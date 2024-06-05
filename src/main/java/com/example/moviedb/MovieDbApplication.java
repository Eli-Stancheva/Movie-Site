package com.example.moviedb;

import com.example.moviedb.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class MovieDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieDbApplication.class, args);
    }

}
