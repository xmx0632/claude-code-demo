package com.todolist.controller;

import com.todolist.common.response.R;
import com.todolist.dto.request.CategoryDTO;
import com.todolist.dto.response.CategoryVO;
import com.todolist.service.ICategoryService;
import javax.validation.Valid;
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
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/list")
    public R<List<CategoryVO>> list() {
        List<CategoryVO> list = categoryService.selectList();
        return R.ok(list);
    }

    @GetMapping("/{id}")
    public R<CategoryVO> getInfo(@PathVariable Long id) {
        CategoryVO vo = categoryService.getById(id);
        return R.ok(vo);
    }

    @PostMapping
    public R<Void> add(@Valid @RequestBody CategoryDTO dto) {
        categoryService.insert(dto);
        return R.<Void>ok("Category created successfully", null);
    }

    @PutMapping
    public R<Void> edit(@Valid @RequestBody CategoryDTO dto) {
        categoryService.update(dto);
        return R.<Void>ok("Category updated successfully", null);
    }

    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        categoryService.deleteByIds(ids);
        return R.<Void>ok("Categories deleted successfully", null);
    }
}
