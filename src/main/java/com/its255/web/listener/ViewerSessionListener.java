package com.its255.web.listener;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.its255.util.LoggingUtil;
import com.its255.viewer.ViewerSession;
import com.its255.web.cleanup.CleanupScheduler;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@Component
public class ViewerSessionListener implements HttpSessionListener {

    private final CleanupScheduler cleanupScheduler;

    public ViewerSessionListener(CleanupScheduler cleanupScheduler) {
        this.cleanupScheduler = cleanupScheduler;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {

    	LoggingUtil.debug(
            "[SESSION-LISTENER] sessionDestroyed fired, thread=" +
            Thread.currentThread().getName()
        );

        ViewerSession vs =
            (ViewerSession) event.getSession().getAttribute("VIEWER_SESSION");

        // Always remove the attribute to avoid leaks
        event.getSession().removeAttribute("VIEWER_SESSION");

        if (vs == null) {
        	LoggingUtil.debug(
                "[SESSION-LISTENER] No ViewerSession attached to session."
            );
            return;
        }
        
        try { 
  		  vs.close(); 
  		  } catch (Exception ignore) {
  			LoggingUtil.error(ignore);
  		  }

  	  // 2. Explicitly break references (important on Windows)
       vs.store = null;
       vs.filter = null;
       vs.pidx = null;
       vs.nav = null;
       vs.lastFiltered = null;

        Path sessionDir = vs.sessionDir;
        vs = null;
        LoggingUtil.debug(
            "[SESSION-LISTENER] Session expired. sessionDir=" + sessionDir
        );


        if (sessionDir != null) {
        	LoggingUtil.debug(
                "[SESSION-LISTENER] Scheduling delayed cleanup for " +
                sessionDir
            );
            cleanupScheduler.scheduleCleanup(sessionDir);
        }
    }
}
