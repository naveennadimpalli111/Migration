package com.its255.web.cleanup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class CleanupExecutor {

    private static final ExecutorService EXECUTOR =
        Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "file-cleanup-thread");
            t.setDaemon(true); // important: doesn't block shutdown
            return t;
        });

    private CleanupExecutor() {}

    public static ExecutorService get() {
        return EXECUTOR;
    }
}
