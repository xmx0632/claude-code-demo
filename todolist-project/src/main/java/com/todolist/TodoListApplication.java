package com.todolist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TodoList Application Main Class
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.todolist.mapper")
public class TodoListApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoListApplication.class, args);
        System.out.println("TodoList Application Started Successfully!");
        System.out.println("Swagger UI: http://localhost:8080/doc.html");
    }
}
