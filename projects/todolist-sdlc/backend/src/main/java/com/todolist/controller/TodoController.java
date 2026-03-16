package com.todolist.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todolist.common.response.R;
import com.todolist.dto.TodoDTO;
import com.todolist.dto.TodoQueryDTO;
import com.todolist.service.TodoService;
import com.todolist.vo.TodoVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 任务控制器
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /**
     * 分页查询任务列表
     */
    @GetMapping
    public R<IPage<TodoVO>> pageList(TodoQueryDTO query) {
        IPage<TodoVO> page = todoService.pageList(query);
        return R.ok(page);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/{id}")
    public R<TodoVO> getDetail(@PathVariable Long id) {
        TodoVO todo = todoService.getDetail(id);
        return R.ok(todo);
    }

    /**
     * 创建任务
     */
    @PostMapping
    public R<TodoVO> create(@Valid @RequestBody TodoDTO dto) {
        TodoVO todo = todoService.create(dto);
        return R.ok(todo, "创建成功");
    }

    /**
     * 更新任务
     */
    @PutMapping("/{id}")
    public R<TodoVO> update(@PathVariable Long id, @Valid @RequestBody TodoDTO dto) {
        TodoVO todo = todoService.update(id, dto);
        return R.ok(todo, "更新成功");
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        todoService.delete(id);
        return R.ok(null, "删除成功");
    }

    /**
     * 完成任务
     */
    @PutMapping("/{id}/complete")
    public R<TodoVO> complete(@PathVariable Long id) {
        TodoVO todo = todoService.complete(id);
        return R.ok(todo, "操作成功");
    }

    /**
     * 取消完成
     */
    @PutMapping("/{id}/uncomplete")
    public R<TodoVO> uncomplete(@PathVariable Long id) {
        TodoVO todo = todoService.uncomplete(id);
        return R.ok(todo, "操作成功");
    }
}
