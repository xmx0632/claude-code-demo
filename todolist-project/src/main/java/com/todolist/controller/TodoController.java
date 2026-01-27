package com.todolist.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todolist.common.response.R;
import com.todolist.common.response.TableDataInfo;
import com.todolist.dto.request.TodoDTO;
import com.todolist.dto.response.TodoVO;
import com.todolist.query.TodoQuery;
import com.todolist.service.ITodoService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Todo Controller
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {

    private final ITodoService todoService;

    @GetMapping("/list")
    public TableDataInfo<TodoVO> list(TodoQuery query) {
        IPage<TodoVO> page = todoService.selectList(query);
        return TableDataInfo.build(page);
    }

    @GetMapping("/{id}")
    public R<TodoVO> getInfo(@PathVariable Long id) {
        TodoVO vo = todoService.getById(id);
        return R.ok(vo);
    }

    @PostMapping
    public R<Void> add(@Valid @RequestBody TodoDTO dto) {
        todoService.insert(dto);
        return R.ok("Todo created successfully");
    }

    @PutMapping
    public R<Void> edit(@Valid @RequestBody TodoDTO dto) {
        todoService.update(dto);
        return R.ok("Todo updated successfully");
    }

    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        todoService.deleteByIds(ids);
        return R.ok("Todos deleted successfully");
    }

    @PatchMapping("/{id}/toggle")
    public R<TodoVO> toggle(@PathVariable Long id) {
        TodoVO vo = todoService.toggleStatus(id);
        return R.ok("Status toggled successfully", vo);
    }
}
