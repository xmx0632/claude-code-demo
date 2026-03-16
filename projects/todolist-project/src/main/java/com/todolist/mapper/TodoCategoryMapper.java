package com.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.domain.entity.TodoCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Todo-Category Association Data Access Interface
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Mapper
public interface TodoCategoryMapper extends BaseMapper<TodoCategory> {

    /**
     * Select category IDs by todo ID
     *
     * @param todoId Todo ID
     * @return Category ID list
     */
    List<Long> selectCategoryIdsByTodoId(@Param("todoId") Long todoId);

    /**
     * Delete associations by todo ID
     *
     * @param todoId Todo ID
     * @return Number of deleted records
     */
    int deleteByTodoId(@Param("todoId") Long todoId);

    /**
     * Batch insert associations
     *
     * @param todoCategories Association list
     * @return Number of inserted records
     */
    int batchInsert(@Param("list") List<TodoCategory> todoCategories);
}
