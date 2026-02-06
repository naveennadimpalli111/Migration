package com.its255.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FileViewerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileViewerApplication.class, args);
    }
}
