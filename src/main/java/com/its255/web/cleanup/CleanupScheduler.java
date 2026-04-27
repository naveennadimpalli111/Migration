package com.its255.web.cleanup;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import com.its255.util.LoggingUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class CleanupScheduler {

    private final CleanupProperties props;
    private ScheduledThreadPoolExecutor executor;

    public CleanupScheduler(CleanupProperties props) {
        this.props = props;
    }

    @PostConstruct
    public void init() {
        this.executor = new ScheduledThreadPoolExecutor(
            props.getWorkerCount(),
            r -> {
                Thread t = new Thread(r, "viewer-cleanup-worker");
                t.setDaemon(true);
                return t;
            }
        );
        LoggingUtil.debug("Initialized cleanup executor with workers=" + props.getWorkerCount());
    }

    public void scheduleCleanup(Path sessionDir) {
    	try {
            long ts = Files.getLastModifiedTime(sessionDir).toMillis();
            LoggingUtil.debug("Scheduling cleanup for: " + sessionDir +
                  " (lastModified=" + ts +
                  ", initialDelayMin=" + props.getInitialDelayMinutes() + ")");
        } catch (Exception e) {
            LoggingUtil.debug("Scheduling cleanup for: " + sessionDir + " (timestamp unavailable)");
        }
        CleanupTask task = new CleanupTask(sessionDir);
        schedule(task, props.getInitialDelayMinutes(), TimeUnit.MINUTES);
    }

    private void schedule(CleanupTask task, long delay, TimeUnit unit) {
    	LoggingUtil.debug("Task scheduled: " + task.sessionDir +
                " delay=" + delay + " " + unit);
        executor.schedule(() -> run(task), delay, unit);
    }

    private void run(CleanupTask task) {
    	LoggingUtil.debug("Worker picked task: " + task.sessionDir +
                " attempt=" + (task.attempts + 1) +
                " thread=" + Thread.currentThread().getName());
        boolean deleted = AsyncFileDeleter.tryDelete(task.sessionDir);

        if (deleted) {
        	LoggingUtil.debug("Cleanup succeeded for: " + task.sessionDir);
            return;
        }

        task.attempts++;
        if (task.attempts < props.getMaxAttempts()) {
        	LoggingUtil.debug("Retrying cleanup for: " + task.sessionDir +
                    " nextAttemptInMs=" + props.getRetryDelayMs() +
                    " attempt=" + (task.attempts + 1));
            schedule(task, props.getRetryDelayMs(), TimeUnit.MILLISECONDS);
        } else {
            // log permanent failure if desired
        	LoggingUtil.debug("PERMANENT FAILURE after " + task.attempts +
                    " attempts: " + task.sessionDir);
        }
    }
}
