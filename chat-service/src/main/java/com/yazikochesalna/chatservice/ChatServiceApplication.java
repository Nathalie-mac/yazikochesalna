package com.yazikochesalna.chatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication (scanBasePackages = {
        "com.yazikochesalna.chatservice",
        "com.yazikochesalna.common"
},
exclude = {ErrorMvcAutoConfiguration.class}
)
public class ChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatServiceApplication.class, args);

    }

}
