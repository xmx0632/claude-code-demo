package com.todolist.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.domain.entity.Todo;
import com.todolist.query.TodoQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Todo Data Access Interface
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Mapper
public interface TodoMapper extends BaseMapper<Todo> {

    /**
     * Select todo list with pagination
     *
     * @param page  Page object
     * @param query Query parameters
     * @return Todo list page
     */
    IPage<Todo> selectListPage(IPage<Todo> page, @Param("query") TodoQuery query);
}
