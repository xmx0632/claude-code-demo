package com.todolist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * TodoList Application Startup Test
 *
 * Verifies that the Spring Boot application context loads successfully.
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
class TodoListApplicationTests {

    /**
     * Test application context loads
     */
    @Test
    void contextLoads() {
        // If this test passes, the application context loaded successfully
    }
}
