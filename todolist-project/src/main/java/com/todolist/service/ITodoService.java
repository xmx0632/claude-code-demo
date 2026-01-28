package com.todolist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todolist.dto.request.TodoDTO;
import com.todolist.dto.response.TodoVO;
import com.todolist.query.TodoQuery;

/**
 * Todo Service Interface
 *
 * @author TodoList Team
 * @since 1.0.0
 */
public interface ITodoService {

    /**
     * Query todo list with pagination
     *
     * @param query Query parameters
     * @return Todo page
     */
    IPage<TodoVO> selectList(TodoQuery query);

    /**
     * Get todo by ID
     *
     * @param id Todo ID
     * @return Todo information
     */
    TodoVO getById(Long id);

    /**
     * Insert todo
     *
     * @param dto Todo request
     * @return Created todo ID
     */
    Long insert(TodoDTO dto);

    /**
     * Update todo
     *
     * @param dto Todo request
     */
    void update(TodoDTO dto);

    /**
     * Batch delete todos
     *
     * @param ids Todo ID array
     */
    void deleteByIds(Long[] ids);

    /**
     * Toggle todo status
     *
     * @param id Todo ID
     * @return Updated todo
     */
    TodoVO toggleStatus(Long id);
}
