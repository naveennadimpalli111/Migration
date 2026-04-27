package com.its255.web.cleanup;


import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.its255.util.LoggingUtil;

public final class AsyncFileDeleter {

    private AsyncFileDeleter() {}

    public static boolean tryDelete(Path root) {
    	LoggingUtil.debug("START delete attempt for: " + root);
        try {
            if (!Files.exists(root)) {
            	LoggingUtil.debug("Path does not exist, nothing to delete: " + root);
                return true;
            }

            List<Path> files = new ArrayList<>();
            List<Path> dirs = new ArrayList<>();

            // ✅ Collect phase (stream fully closed before delete)
            try (var stream = Files.walk(root)) {
                stream.forEach(p -> {
                	long ts = 0;
					try {
						ts = Files.getLastModifiedTime(p).toMillis();
					} catch (IOException e) {
						LoggingUtil.error(e);
					}
                    if (Files.isDirectory(p)) {
                        dirs.add(p);
                        LoggingUtil.debug("Queued DIR  for delete: " + p + " (lastModified=" + ts + ")");
                    } else {
                        files.add(p);
                        LoggingUtil.debug("Queued FILE for delete: " + p + " (lastModified=" + ts + ")");	
                    }
                });
            } catch (Exception e) {
                LoggingUtil.debug("Failed to stat path: " + e);
            }

            // ✅ Delete files first
            for (Path file : files) {
            	LoggingUtil.debug("Deleting FILE: " + file);
                Files.deleteIfExists(file);
            }
            	
            // ✅ Delete directories bottom-up
            dirs.sort(Comparator.reverseOrder());
            for (Path dir : dirs) {
                Files.deleteIfExists(dir);
            }
            LoggingUtil.debug("SUCCESS delete: " + root);
            return true;

        } catch (Exception ex) {
        	LoggingUtil.debug("FAILED delete: " + root);
            LoggingUtil.error(ex);
            return false;
        }
    }
}
