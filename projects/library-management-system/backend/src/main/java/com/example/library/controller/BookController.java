package com.example.library.controller;

import com.example.library.common.Result;
import com.example.library.dto.request.BookRequest;
import com.example.library.dto.request.StockOperationRequest;
import com.example.library.dto.response.BookResponse;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 图书控制器
 *
 * @author Claude Code
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * 获取图书列表（分页）
     */
    @GetMapping
    public Result<Map<String, Object>> findAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return Result.success(bookService.findAll(page, size, sortBy, sortOrder));
    }

    /**
     * 搜索图书
     */
    @GetMapping("/search")
    public Result<Map<String, Object>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(bookService.search(title, author, categoryId, page, size));
    }

    /**
     * 获取图书详情
     */
    @GetMapping("/{id}")
    public Result<BookResponse> findById(@PathVariable Long id) {
        return Result.success(bookService.findById(id));
    }

    /**
     * 新增图书
     */
    @PostMapping
    public Result<BookResponse> create(@Valid @RequestBody BookRequest request) {
        return Result.success(bookService.create(request));
    }

    /**
     * 更新图书
     */
    @PutMapping("/{id}")
    public Result<BookResponse> update(@PathVariable Long id,
                                       @Valid @RequestBody BookRequest request) {
        return Result.success(bookService.update(id, request));
    }

    /**
     * 删除图书
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return Result.success();
    }

    /**
     * 入库操作
     */
    @PostMapping("/{id}/stock-in")
    public Result<Void> stockIn(@PathVariable Long id,
                                @Valid @RequestBody StockOperationRequest request) {
        bookService.stockIn(id, request);
        return Result.success("入库成功", null);
    }

    /**
     * 出库操作
     */
    @PostMapping("/{id}/stock-out")
    public Result<Void> stockOut(@PathVariable Long id,
                                @Valid @RequestBody StockOperationRequest request) {
        bookService.stockOut(id, request);
        return Result.success("出库成功", null);
    }
}
