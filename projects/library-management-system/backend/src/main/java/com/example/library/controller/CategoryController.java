package com.example.library.controller;

import com.example.library.common.Result;
import com.example.library.dto.request.CategoryRequest;
import com.example.library.dto.response.CategoryResponse;
import com.example.library.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 *
 * @author Claude Code
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 获取所有分类
     */
    @GetMapping
    public Result<List<CategoryResponse>> findAll() {
        return Result.success(categoryService.findAll());
    }

    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    public Result<CategoryResponse> findById(@PathVariable Long id) {
        return Result.success(categoryService.findById(id));
    }

    /**
     * 新增分类
     */
    @PostMapping
    public Result<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        return Result.success(categoryService.create(request));
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public Result<CategoryResponse> update(@PathVariable Long id,
                                          @Valid @RequestBody CategoryRequest request) {
        return Result.success(categoryService.update(id, request));
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }
}
