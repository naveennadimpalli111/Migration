package com.its255.web.storage;

import java.nio.file.Path;

public interface ObjectStorageClient {
    void headBucket(String bucketName);

    void putObject(String bucketName, String key, Path source, long contentLength, String contentType);

    long headObjectContentLength(String bucketName, String key);

    void downloadObject(String bucketName, String key, Path target);
}
