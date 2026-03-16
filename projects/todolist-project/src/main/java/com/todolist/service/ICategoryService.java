package com.todolist.service;

import com.todolist.dto.request.CategoryDTO;
import com.todolist.dto.response.CategoryVO;

import java.util.List;

/**
 * Category Service Interface
 *
 * @author TodoList Team
 * @since 1.0.0
 */
public interface ICategoryService {

    /**
     * Query category list
     *
     * @return Category list
     */
    List<CategoryVO> selectList();

    /**
     * Get category by ID
     *
     * @param id Category ID
     * @return Category information
     */
    CategoryVO getById(Long id);

    /**
     * Insert category
     *
     * @param dto Category request
     * @return Created category ID
     */
    Long insert(CategoryDTO dto);

    /**
     * Update category
     *
     * @param dto Category request
     */
    void update(CategoryDTO dto);

    /**
     * Batch delete categories
     *
     * @param ids Category ID array
     */
    void deleteByIds(Long[] ids);
}
