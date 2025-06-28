package com.yazikochesalna.messagestorageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = {
        "com.yazikochesalna.messagestorageservice",
        "com.yazikochesalna.common"
})
public class MessageStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageStorageServiceApplication.class, args);
    }

}
