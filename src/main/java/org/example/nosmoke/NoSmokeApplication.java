package org.example.nosmoke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NoSmokeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoSmokeApplication.class, args);
    }

}
