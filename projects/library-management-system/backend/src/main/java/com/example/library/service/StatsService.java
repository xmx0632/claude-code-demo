package com.example.library.service;

import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.repository.StockLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 统计业务逻辑类
 *
 * @author Claude Code
 */
@Service
@RequiredArgsConstructor
public class StatsService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final StockLogRepository stockLogRepository;

    /**
     * 获取统计概览
     */
    public Map<String, Object> getSummary() {
        long totalBooks = bookRepository.count();
        long totalCategories = categoryRepository.count();
        long lowStockCount = bookRepository.findLowStockBooks().size();
        long totalStockLogs = stockLogRepository.count();

        return Map.of(
                "totalBooks", totalBooks,
                "totalCategories", totalCategories,
                "lowStockCount", lowStockCount,
                "totalStockLogs", totalStockLogs
        );
    }
}
