package com.todolist.controller;

import com.todolist.common.response.R;
import com.todolist.dto.request.CategoryDTO;
import com.todolist.dto.response.CategoryVO;
import com.todolist.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category Controller
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "Category CRUD endpoints")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "Query Category List")
    public R<List<CategoryVO>> list() {
        List<CategoryVO> list = categoryService.selectList();
        return R.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Category Details")
    public R<CategoryVO> getInfo(@PathVariable Long id) {
        CategoryVO vo = categoryService.getById(id);
        return R.ok(vo);
    }

    @PostMapping
    @Operation(summary = "Create Category")
    public R<Void> add(@Valid @RequestBody CategoryDTO dto) {
        categoryService.insert(dto);
        return R.ok("Category created successfully");
    }

    @PutMapping
    @Operation(summary = "Update Category")
    public R<Void> edit(@Valid @RequestBody CategoryDTO dto) {
        categoryService.update(dto);
        return R.ok("Category updated successfully");
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "Delete Categories")
    public R<Void> remove(@PathVariable Long[] ids) {
        categoryService.deleteByIds(ids);
        return R.ok("Categories deleted successfully");
    }
}
