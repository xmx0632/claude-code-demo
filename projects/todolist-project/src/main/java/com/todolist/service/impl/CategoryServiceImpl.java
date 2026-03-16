package com.todolist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.common.exception.BusinessException;
import com.todolist.common.util.BeanConv;
import com.todolist.domain.entity.Category;
import com.todolist.domain.entity.TodoCategory;
import com.todolist.dto.request.CategoryDTO;
import com.todolist.dto.response.CategoryVO;
import com.todolist.mapper.CategoryMapper;
import com.todolist.mapper.TodoCategoryMapper;
import com.todolist.security.SecurityUtils;
import com.todolist.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Category Service Implementation
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryMapper categoryMapper;
    private final TodoCategoryMapper todoCategoryMapper;

    @Override
    public List<CategoryVO> selectList() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        List<Category> categories = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getUserId, userId)
                        .orderByDesc(Category::getCreatedAt)
        );

        return categories.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryVO getById(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "Category not found");
        }

        // Data isolation check
        if (!category.getUserId().equals(userId)) {
            throw new BusinessException(403, "No permission to access this category");
        }

        return convertToVO(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(CategoryDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        // Check name uniqueness within user's categories
        Long count = categoryMapper.selectCount(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getUserId, userId)
                        .eq(Category::getName, dto.getName())
        );
        if (count > 0) {
            throw new BusinessException(409, "Category name already exists");
        }

        // Build category entity
        Category category = Category.builder()
                .userId(userId)
                .name(dto.getName())
                .color(dto.getColor() != null ? dto.getColor() : "#000000")
                .build();

        categoryMapper.insert(category);

        log.info("Category created: id={}, name={}", category.getId(), category.getName());
        return category.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CategoryDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("Category ID cannot be empty");
        }

        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        Category existingCategory = categoryMapper.selectById(dto.getId());
        if (existingCategory == null) {
            throw new BusinessException(404, "Category not found");
        }

        // Data isolation check
        if (!existingCategory.getUserId().equals(userId)) {
            throw new BusinessException(403, "No permission to modify this category");
        }

        // Check name uniqueness
        if (!existingCategory.getName().equals(dto.getName())) {
            Long count = categoryMapper.selectCount(
                    new LambdaQueryWrapper<Category>()
                            .eq(Category::getUserId, userId)
                            .eq(Category::getName, dto.getName())
                            .ne(Category::getId, dto.getId())
            );
            if (count > 0) {
                throw new BusinessException(409, "Category name already exists");
            }
        }

        // Update category
        existingCategory.setName(dto.getName());
        if (dto.getColor() != null) {
            existingCategory.setColor(dto.getColor());
        }

        categoryMapper.updateById(existingCategory);

        log.info("Category updated: id={}", dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }

        for (Long id : ids) {
            Category category = categoryMapper.selectById(id);
            if (category != null && category.getUserId().equals(userId)) {
                // Check if category is being used
                Long count = todoCategoryMapper.selectCount(
                        new LambdaQueryWrapper<TodoCategory>()
                                .eq(TodoCategory::getCategoryId, id)
                );
                if (count > 0) {
                    throw new BusinessException(409, "Category is in use and cannot be deleted");
                }

                categoryMapper.deleteById(id);
            }
        }

        log.info("Categories deleted: ids={}", ids);
    }

    /**
     * Convert Category entity to VO with todo count
     */
    private CategoryVO convertToVO(Category category) {
        CategoryVO vo = BeanConv.convert(category, CategoryVO.class);

        // Count todos in this category
        Long count = todoCategoryMapper.selectCount(
                new LambdaQueryWrapper<TodoCategory>()
                        .eq(TodoCategory::getCategoryId, category.getId())
        );
        vo.setTodoCount(count.intValue());

        return vo;
    }
}
