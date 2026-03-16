package com.todolist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.todolist.dto.TodoDTO;
import com.todolist.dto.TodoQueryDTO;
import com.todolist.entity.Todo;
import com.todolist.vo.TodoVO;

/**
 * 任务服务接口
 *
 * @author Claude Code
 * @since 2026-03-16
 */
public interface TodoService extends IService<Todo> {

    /**
     * 分页查询任务列表
     */
    IPage<TodoVO> pageList(TodoQueryDTO query);

    /**
     * 获取任务详情
     */
    TodoVO getDetail(Long id);

    /**
     * 创建任务
     */
    TodoVO create(TodoDTO dto);

    /**
     * 更新任务
     */
    TodoVO update(Long id, TodoDTO dto);

    /**
     * 删除任务
     */
    void delete(Long id);

    /**
     * 完成任务
     */
    TodoVO complete(Long id);

    /**
     * 取消完成
     */
    TodoVO uncomplete(Long id);
}
