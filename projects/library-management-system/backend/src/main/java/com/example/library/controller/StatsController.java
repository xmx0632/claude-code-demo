package com.example.library.controller;

import com.example.library.common.Result;
import com.example.library.dto.response.BookResponse;
import com.example.library.service.BookService;
import com.example.library.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 统计控制器
 *
 * @author Claude Code
 */
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final BookService bookService;

    /**
     * 获取统计概览
     */
    @GetMapping("/summary")
    public Result<Map<String, Object>> getSummary() {
        return Result.success(statsService.getSummary());
    }

    /**
     * 获取低库存图书
     */
    @GetMapping("/low-stock")
    public Result<List<BookResponse>> getLowStockBooks() {
        return Result.success(bookService.getLowStockBooks());
    }
}
