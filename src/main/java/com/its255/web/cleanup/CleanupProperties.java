package com.its255.web.cleanup;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "viewer.cleanup")
public class CleanupProperties {

    private int initialDelayMinutes;
    private long retryDelayMs;
    private int maxAttempts;
    private int workerCount;
    private int startupCleanupMinutes;

    public int getInitialDelayMinutes() {
        return initialDelayMinutes;
    }

    public void setInitialDelayMinutes(int initialDelayMinutes) {
        this.initialDelayMinutes = initialDelayMinutes;
    }

    public long getRetryDelayMs() {
        return retryDelayMs;
    }

    public void setRetryDelayMs(long retryDelayMs) {
        this.retryDelayMs = retryDelayMs;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public int getStartupCleanupMinutes() {
        return startupCleanupMinutes;
    }

    public void setStartupCleanupMinutes(int startupCleanupMinutes) {
        this.startupCleanupMinutes = startupCleanupMinutes;
    }
}
