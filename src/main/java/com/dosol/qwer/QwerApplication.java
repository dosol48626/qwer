package com.dosol.qwer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QwerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QwerApplication.class, args);
    }

}
