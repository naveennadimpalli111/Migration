package com.its255.web.cleanup;

import java.nio.file.Path;

final class CleanupTask {

    final Path sessionDir;
    int attempts = 0;

    CleanupTask(Path sessionDir) {
        this.sessionDir = sessionDir;
    }
}
