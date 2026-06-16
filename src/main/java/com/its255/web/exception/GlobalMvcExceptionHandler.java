package com.its255.web.exception;

import java.io.EOFException;

import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.its255.web.storage.StorageException;

@ControllerAdvice
public class GlobalMvcExceptionHandler {
	
	@ExceptionHandler(IllegalArgumentException.class)
	public String illegalArgumentException(IllegalArgumentException ex, Model model) {
		System.err.println(ex.getMessage());
		model.addAttribute("title", "Fixed Parser Application");
		model.addAttribute("issue", "Something went wrong");
		model.addAttribute("message", ex.getMessage());
		return "error";
	}
	
	@ExceptionHandler(EOFException.class)
	public String eofException(EOFException ex, Model model) {
		System.err.println(ex.getMessage());
		model.addAttribute("title", "Fixed Parser Application");
		model.addAttribute("issue", "Something went wrong");
		model.addAttribute("message", ex.getMessage());
		return "error";
	}

	@ExceptionHandler(StorageException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String storageException(StorageException ex, Model model) {
		System.err.println(ex.getMessage());
		model.addAttribute("title", "Fixed Parser Application");
		model.addAttribute("issue", "AWS S3 storage error");
		model.addAttribute("message", ex.getMessage());
		return "error";
	}

}
