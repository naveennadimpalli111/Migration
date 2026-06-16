package com.its255.web.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import com.its255.util.LoggingUtil;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3FileStorageService {
    private static final DateTimeFormatter KEY_DATE = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final S3Properties properties;
    private final ObjectProvider<ObjectStorageClient> storageClientProvider;

    public S3FileStorageService(S3Properties properties, ObjectProvider<ObjectStorageClient> storageClientProvider) {
        this.properties = properties;
        this.storageClientProvider = storageClientProvider;
    }

    public boolean isEnabled() {
        return properties.isEnabled();
    }

    public String bucketName() {
        return properties.getBucketName();
    }

    public void verifyConnection() {
        if (!isEnabled()) {
            return;
        }
        validateConfiguration();
        try {
            client().headBucket(properties.getBucketName());
            LoggingUtil.debug("AWS S3 connection verified. bucket=" + properties.getBucketName());
        } catch (RuntimeException ex) {
            throw translate("Unable to connect to configured AWS S3 bucket", ex);
        }
    }

    public String uploadNewFile(Path source, String originalFilename) {
        String key = buildKey(originalFilename);
        uploadFile(source, key);
        return key;
    }

    public void uploadUpdatedFile(Path source, String key) {
        if (!isEnabled()) {
            return;
        }
        if (key == null || key.isBlank()) {
            throw new StorageException("S3 file key is missing for update.");
        }
        uploadFile(source, key);
    }

    public void downloadToFile(String key, Path target) {
        if (!isEnabled()) {
            return;
        }
        if (key == null || key.isBlank()) {
            throw new StorageException("S3 file key is missing for download.");
        }
        validateConfiguration();
        try {
            Files.deleteIfExists(target);
            client().downloadObject(properties.getBucketName(), key, target);
            long expectedLength = client().headObjectContentLength(properties.getBucketName(), key);
            long downloadedLength = Files.size(target);
            if (downloadedLength != expectedLength) {
                throw new StorageException("S3 download integrity check failed for key " + key
                        + ". Expected " + expectedLength + " bytes but downloaded " + downloadedLength + " bytes.");
            }
            LoggingUtil.debug("Downloaded and verified S3 object. bucket=" + properties.getBucketName()
                    + ", key=" + key + ", bytes=" + downloadedLength);
        } catch (IOException ex) {
            throw new StorageException("Unable to verify downloaded S3 file: " + key, ex);
        } catch (RuntimeException ex) {
            if (ex instanceof StorageException storageException) {
                throw storageException;
            }
            throw translate("Unable to download file from AWS S3", ex);
        }
    }

    private void uploadFile(Path source, String key) {
        if (!isEnabled()) {
            return;
        }
        validateConfiguration();
        try {
            long length = Files.size(source);
            client().putObject(properties.getBucketName(), key, source, length, "application/octet-stream");
            long uploadedLength = client().headObjectContentLength(properties.getBucketName(), key);
            if (uploadedLength != length) {
                throw new StorageException("S3 upload integrity check failed for key " + key
                        + ". Expected " + length + " bytes but bucket reports " + uploadedLength + " bytes.");
            }
            LoggingUtil.debug("Uploaded and verified S3 object. bucket=" + properties.getBucketName()
                    + ", key=" + key + ", bytes=" + length);
        } catch (IOException ex) {
            throw new StorageException("Unable to read local file before S3 upload: " + source, ex);
        } catch (RuntimeException ex) {
            if (ex instanceof StorageException storageException) {
                throw storageException;
            }
            throw translate("Unable to upload file to AWS S3", ex);
        }
    }

    private ObjectStorageClient client() {
        ObjectStorageClient storageClient = storageClientProvider.getIfAvailable();
        if (storageClient == null) {
            throw new StorageException("AWS S3 is enabled, but no S3 client bean is available.");
        }
        return storageClient;
    }

    private void validateConfiguration() {
        if (properties.getBucketName() == null || properties.getBucketName().isBlank()) {
            throw new StorageException("AWS S3 bucket name is not configured.");
        }
        if (properties.getRegion() == null || properties.getRegion().isBlank()) {
            throw new StorageException("AWS S3 region is not configured.");
        }
    }

    private String buildKey(String originalFilename) {
        String prefix = normalizePrefix(properties.getKeyPrefix());
        String datePath = LocalDate.now().format(KEY_DATE);
        String safeFilename = sanitizeFilename(
                (originalFilename == null || originalFilename.isBlank()) ? "uploaded.dat" : originalFilename);
        return prefix + "/" + datePath + "/" + UUID.randomUUID() + "-" + safeFilename;
    }

    private String normalizePrefix(String prefix) {
        String value = (prefix == null || prefix.isBlank()) ? "uploads" : prefix.trim();
        value = value.replace('\\', '/');
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value.isBlank() ? "uploads" : value;
    }

    private String sanitizeFilename(String filename) {
        String safe = Objects.toString(filename, "uploaded.dat")
                .replace('\\', '_')
                .replace('/', '_')
                .replaceAll("[^A-Za-z0-9._-]", "_");
        return safe.isBlank() ? "uploaded.dat" : safe;
    }

    private StorageException translate(String action, RuntimeException ex) {
        LoggingUtil.error(action, ex);

        if (ex instanceof NoSuchBucketException) {
            return new StorageException(action + ": configured S3 bucket was not found: "
                    + properties.getBucketName(), ex);
        }
        if (ex instanceof NoSuchKeyException) {
            return new StorageException(action + ": file was not found in S3.", ex);
        }
        if (ex instanceof S3Exception s3Exception) {
            int statusCode = s3Exception.statusCode();
            String awsCode = s3Exception.awsErrorDetails() != null
                    ? s3Exception.awsErrorDetails().errorCode()
                    : "";
            if (statusCode == 403) {
                return new StorageException(action
                        + ": invalid AWS credentials or access denied for bucket "
                        + properties.getBucketName() + ".", ex);
            }
            if (statusCode == 404 || "NoSuchBucket".equals(awsCode)) {
                return new StorageException(action + ": configured S3 bucket was not found: "
                        + properties.getBucketName(), ex);
            }
            return new StorageException(action + ": AWS S3 error " + statusCode + " " + awsCode
                    + " - " + s3Exception.getMessage(), ex);
        }
        if (ex instanceof AwsServiceException awsException) {
            return new StorageException(action + ": AWS service error " + awsException.statusCode()
                    + " - " + awsException.getMessage(), ex);
        }
        if (ex instanceof SdkClientException) {
            return new StorageException(action
                    + ": AWS SDK client error. Check credentials, region, network, and bucket settings.", ex);
        }
        return new StorageException(action + ": " + ex.getMessage(), ex);
    }
}
