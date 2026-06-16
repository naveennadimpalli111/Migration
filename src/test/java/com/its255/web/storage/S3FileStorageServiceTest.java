package com.its255.web.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.ObjectProvider;

class S3FileStorageServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void uploadNewFileStoresObjectAndReturnsKey() throws Exception {
        FakeObjectStorageClient client = new FakeObjectStorageClient();
        S3FileStorageService service = service(client);
        Path source = tempDir.resolve("input.dat");
        byte[] bytes = "original".getBytes();
        Files.write(source, bytes);

        String key = service.uploadNewFile(source, "claim file.dat");

        assertEquals("test-bucket", client.lastBucket);
        assertArrayEquals(bytes, client.objects.get(key));
        assertTrue(key.startsWith("claims/"));
        assertTrue(key.endsWith("-claim_file.dat"));
    }

    @Test
    void uploadUpdatedFileOverwritesSameObjectKey() throws Exception {
        FakeObjectStorageClient client = new FakeObjectStorageClient();
        S3FileStorageService service = service(client);
        Path original = tempDir.resolve("original.dat");
        Path updated = tempDir.resolve("updated.dat");
        Files.write(original, "before".getBytes());
        Files.write(updated, "after-delete-or-edit".getBytes());

        String key = service.uploadNewFile(original, "input.dat");
        service.uploadUpdatedFile(updated, key);

        assertArrayEquals("after-delete-or-edit".getBytes(), client.objects.get(key));
    }

    @Test
    void downloadToFileVerifiesDownloadedSize() throws Exception {
        FakeObjectStorageClient client = new FakeObjectStorageClient();
        S3FileStorageService service = service(client);
        Path source = tempDir.resolve("input.dat");
        Path downloaded = tempDir.resolve("downloaded.dat");
        byte[] bytes = "download-me".getBytes();
        Files.write(source, bytes);
        String key = service.uploadNewFile(source, "input.dat");

        service.downloadToFile(key, downloaded);

        assertArrayEquals(bytes, Files.readAllBytes(downloaded));
    }

    @Test
    void downloadToFileFailsWhenIntegrityCheckDoesNotMatch() throws Exception {
        FakeObjectStorageClient client = new FakeObjectStorageClient();
        S3FileStorageService service = service(client);
        Path source = tempDir.resolve("input.dat");
        Path downloaded = tempDir.resolve("downloaded.dat");
        Files.write(source, "abc".getBytes());
        String key = service.uploadNewFile(source, "input.dat");
        client.reportedLengths.put(key, 99L);

        StorageException ex = assertThrows(StorageException.class, () -> service.downloadToFile(key, downloaded));

        assertTrue(ex.getMessage().contains("integrity check failed"));
    }

    @SuppressWarnings("unchecked")
    private S3FileStorageService service(FakeObjectStorageClient client) {
        S3Properties properties = new S3Properties();
        properties.setEnabled(true);
        properties.setBucketName("test-bucket");
        properties.setRegion("us-east-1");
        properties.setKeyPrefix("claims");

        ObjectProvider<ObjectStorageClient> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(client);
        return new S3FileStorageService(properties, provider);
    }

    private static class FakeObjectStorageClient implements ObjectStorageClient {
        private final Map<String, byte[]> objects = new HashMap<>();
        private final Map<String, Long> reportedLengths = new HashMap<>();
        private String lastBucket;

        @Override
        public void headBucket(String bucketName) {
            lastBucket = bucketName;
        }

        @Override
        public void putObject(String bucketName, String key, Path source, long contentLength, String contentType) {
            lastBucket = bucketName;
            try {
                byte[] bytes = Files.readAllBytes(source);
                objects.put(key, bytes);
                reportedLengths.put(key, (long) bytes.length);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public long headObjectContentLength(String bucketName, String key) {
            lastBucket = bucketName;
            return reportedLengths.getOrDefault(key, -1L);
        }

        @Override
        public void downloadObject(String bucketName, String key, Path target) {
            lastBucket = bucketName;
            byte[] bytes = objects.get(key);
            if (bytes == null) {
                throw new RuntimeException("missing key");
            }
            try {
                Files.write(target, bytes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
