package com.its255.web.startup;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.its255.util.LoggingUtil;

import jakarta.annotation.PostConstruct;

@Component
public class ViewerTempStartupCleaner {

    @PostConstruct
	public void cleanup() throws Exception {
    	cleanupOrphans();  
	    cleanupLegacyTempFiles(); // (temporary)
	}

    public void cleanupOrphans() throws Exception {
        Path root = Paths.get(
            System.getProperty("java.io.tmpdir"),
            "its255Files"
        );

        if (!Files.exists(root)) return;

        long cutoff =
            System.currentTimeMillis() -
            TimeUnit.MINUTES.toMillis(1);

        try (DirectoryStream<Path> dirs =
                 Files.newDirectoryStream(root, "session_*")) {

            for (Path dir : dirs) {
                long lastModified =
                    Files.getLastModifiedTime(dir).toMillis();

                if (lastModified < cutoff) {
                    LoggingUtil.debug(
                        "PHASE4_STARTUP_CLEAN: deleting orphan " + dir
                    );

                    Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException e) {
                            LoggingUtil.debug(
                                "PHASE4_STARTUP_CLEAN: Failed to delete " + p + " due to " + e.getMessage()
                            );
                        }
                    });
                }
            }
        }
    }
    
    private void cleanupLegacyTempFiles() {
        Path osTempDir = Paths.get(System.getProperty("java.io.tmpdir"));

        long cutoffTime =
            System.currentTimeMillis() -
            TimeUnit.HOURS.toMillis(24); // safe one-time TTL

        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(osTempDir, "its255_v3_*.dat")) {

            for (Path legacyFile : stream) {
                long lastModified =
                    Files.getLastModifiedTime(legacyFile).toMillis();

                if (lastModified < cutoffTime) {
                	LoggingUtil.debug(
                        "PHASE5_LEGACY_CLEANUP: deleting " + legacyFile
                    );

                    try {
                        Files.deleteIfExists(legacyFile);
                    } catch (IOException e) {
                        LoggingUtil.debug(
                            "PHASE5_LEGACY_CLEANUP: failed to delete "
                            + legacyFile + " due to " + e.getMessage()
                        );
                    }
                }
            }
        } catch (IOException e) {
            LoggingUtil.debug(
                "PHASE5_LEGACY_CLEANUP: scan failed due to " + e.getMessage()
            );
        }
    }
}
