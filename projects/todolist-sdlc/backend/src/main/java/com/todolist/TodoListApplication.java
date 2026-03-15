package com.todolist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TodoList 应用启动类
 *
 * @author Claude Code (SDLC Framework Demo)
 * @since 2026-03-16
 */
@SpringBootApplication
@MapperScan("com.todolist.mapper")
public class TodoListApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoListApplication.class, args);
    }
}
