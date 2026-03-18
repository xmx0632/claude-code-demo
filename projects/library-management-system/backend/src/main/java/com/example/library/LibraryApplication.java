package com.example.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 图书库存管理系统 - 启动类
 *
 * @author Claude Code
 * @version 1.0.0
 */
@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("图书库存管理系统启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("========================================\n");
    }
}
