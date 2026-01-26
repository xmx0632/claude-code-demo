package com.todolist.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todolist.common.response.R;
import com.todolist.common.response.TableDataInfo;
import com.todolist.dto.request.TodoDTO;
import com.todolist.dto.response.TodoVO;
import com.todolist.query.TodoQuery;
import com.todolist.service.ITodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Todo Management", description = "Todo CRUD endpoints")
public class TodoController {

    private final ITodoService todoService;

    @GetMapping("/list")
    @Operation(summary = "Query Todo List")
    public TableDataInfo<TodoVO> list(TodoQuery query) {
        IPage<TodoVO> page = todoService.selectList(query);
        return TableDataInfo.build(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Todo Details")
    public R<TodoVO> getInfo(@PathVariable Long id) {
        TodoVO vo = todoService.getById(id);
        return R.ok(vo);
    }

    @PostMapping
    @Operation(summary = "Create Todo")
    public R<Void> add(@Valid @RequestBody TodoDTO dto) {
        todoService.insert(dto);
        return R.ok("Todo created successfully");
    }

    @PutMapping
    @Operation(summary = "Update Todo")
    public R<Void> edit(@Valid @RequestBody TodoDTO dto) {
        todoService.update(dto);
        return R.ok("Todo updated successfully");
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "Delete Todos")
    public R<Void> remove(@PathVariable Long[] ids) {
        todoService.deleteByIds(ids);
        return R.ok("Todos deleted successfully");
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Toggle Todo Status")
    public R<TodoVO> toggle(@PathVariable Long id) {
        TodoVO vo = todoService.toggleStatus(id);
        return R.ok("Status toggled successfully", vo);
    }
}
