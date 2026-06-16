package com.its255.web.storage;

import java.nio.file.Path;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@ConditionalOnProperty(name = "aws.s3.enabled", havingValue = "true")
class AwsS3ObjectStorageClient implements ObjectStorageClient {
    private final S3Client s3Client;

    AwsS3ObjectStorageClient(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void headBucket(String bucketName) {
        s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
    }

    @Override
    public void putObject(String bucketName, String key, Path source, long contentLength, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentLength(contentLength)
                .contentType(contentType)
                .build();
        s3Client.putObject(request, RequestBody.fromFile(source));
    }

    @Override
    public long headObjectContentLength(String bucketName, String key) {
        return s3Client.headObject(HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()).contentLength();
    }

    @Override
    public void downloadObject(String bucketName, String key, Path target) {
        s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(), ResponseTransformer.toFile(target));
    }
}
