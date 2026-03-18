package com.example.library.service;

import com.example.library.dto.request.CategoryRequest;
import com.example.library.dto.response.CategoryResponse;
import com.example.library.entity.Category;
import com.example.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类业务逻辑类
 *
 * @author Claude Code
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 获取所有分类
     */
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取分类
     */
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        return CategoryResponse.from(category);
    }

    /**
     * 创建分类
     */
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        // 检查名称是否已存在
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("分类名称已存在");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category saved = categoryRepository.save(category);
        return CategoryResponse.from(saved);
    }

    /**
     * 更新分类
     */
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        // 检查名称是否被其他分类使用
        if (!category.getName().equals(request.getName()) &&
            categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("分类名称已存在");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updated = categoryRepository.save(category);
        return CategoryResponse.from(updated);
    }

    /**
     * 删除分类
     */
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("分类不存在");
        }
        // TODO: 检查是否有图书使用该分类
        categoryRepository.deleteById(id);
    }
}
