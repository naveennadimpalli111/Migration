package com.its255.util;

public class LoggingUtil {
	
	public static void debug(String msg) {
	    System.out.println("[" + java.time.Instant.now() + "] " + msg);
	}
	
	public static void error(Exception e) {
	    System.out.println(" ERROR MESSAGE: " + e.getMessage());
	}

	public static void error(String message, Exception e) {
	    System.out.println(" ERROR MESSAGE: " + message + " - " + e.getMessage());
	}

}
