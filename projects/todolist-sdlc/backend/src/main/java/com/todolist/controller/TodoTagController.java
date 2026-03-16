package com.todolist.controller;

import com.todolist.common.response.R;
import com.todolist.dto.TodoTagsDTO;
import com.todolist.service.TodoTagService;
import com.todolist.vo.TagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务标签关联控制器
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Tag(name = "任务标签关联", description = "为任务添加、移除、查询标签")
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoTagController {

    private final TodoTagService todoTagService;

    /**
     * 为任务添加标签
     */
    @Operation(summary = "为任务添加标签")
    @PostMapping("/{todoId}/tags")
    public R<List<TagVO>> addTags(
            @PathVariable Long todoId,
            @Valid @RequestBody TodoTagsDTO dto) {
        List<TagVO> tags = todoTagService.addTags(todoId, dto);
        return R.ok(tags, "添加成功");
    }

    /**
     * 移除任务标签
     */
    @Operation(summary = "移除任务标签")
    @DeleteMapping("/{todoId}/tags/{tagId}")
    public R<Void> removeTag(
            @PathVariable Long todoId,
            @PathVariable Long tagId) {
        todoTagService.removeTag(todoId, tagId);
        return R.ok(null, "移除成功");
    }

    /**
     * 查询任务的所有标签
     */
    @Operation(summary = "查询任务的所有标签")
    @GetMapping("/{todoId}/tags")
    public R<List<TagVO>> getTagsByTodoId(@PathVariable Long todoId) {
        List<TagVO> tags = todoTagService.getTagsByTodoId(todoId);
        return R.ok(tags);
    }

    /**
     * 批量更新任务标签（完全替换）
     */
    @Operation(summary = "批量更新任务标签")
    @PutMapping("/{todoId}/tags")
    public R<List<TagVO>> updateTags(
            @PathVariable Long todoId,
            @Valid @RequestBody TodoTagsDTO dto) {
        List<TagVO> tags = todoTagService.updateTags(todoId, dto);
        return R.ok(tags, "更新成功");
    }
}
