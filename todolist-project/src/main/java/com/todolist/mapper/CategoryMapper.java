package com.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * Category Data Access Interface
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    // Inherits CRUD methods from BaseMapper
}
