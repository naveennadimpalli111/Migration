package com.its255.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.its255.viewer.ViewerConfig;
import com.its255.web.storage.S3Properties;


@EnableCaching
@SpringBootApplication
@EnableConfigurationProperties({ViewerConfig.class, S3Properties.class})
public class FileViewerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileViewerApplication.class, args);
    }
}
