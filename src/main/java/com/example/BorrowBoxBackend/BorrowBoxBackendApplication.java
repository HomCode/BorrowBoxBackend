package com.example.BorrowBoxBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BorrowBoxBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BorrowBoxBackendApplication.class, args);
        System.out.println("=================================");
        System.out.println("🚀 BorrowBox Backend Started!");
        System.out.println("📍 Login API: http://localhost:8080/api/auth/login");
        System.out.println("📍 Register API: http://localhost:8080/api/auth/register");
        System.out.println("=================================");
    }
}