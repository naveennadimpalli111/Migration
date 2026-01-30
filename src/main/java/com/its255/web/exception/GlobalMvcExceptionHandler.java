package com.its255.web.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalMvcExceptionHandler {
	
	@ExceptionHandler(IllegalArgumentException.class)
	public String illegalArgumentException(IllegalArgumentException ex, Model model) {
		System.err.println(ex.getMessage());
		model.addAttribute("title", "Fixed Parser Application");
		model.addAttribute("message", ex.getMessage());
		return "error";
	}

}
