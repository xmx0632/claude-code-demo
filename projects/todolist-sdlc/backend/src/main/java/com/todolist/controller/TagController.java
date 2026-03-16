package com.todolist.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todolist.common.response.R;
import com.todolist.dto.TagDTO;
import com.todolist.dto.TagQueryDTO;
import com.todolist.service.TagService;
import com.todolist.vo.TagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务标签控制器
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Tag(name = "任务标签管理", description = "任务标签的增删改查接口")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 分页查询标签列表
     */
    @Operation(summary = "分页查询标签列表")
    @GetMapping
    public R<IPage<TagVO>> pageList(TagQueryDTO query) {
        IPage<TagVO> page = tagService.pageList(query);
        return R.ok(page);
    }

    /**
     * 查询用户的所有标签
     */
    @Operation(summary = "查询用户的所有标签")
    @GetMapping("/all")
    public R<List<TagVO>> listAll() {
        List<TagVO> list = tagService.listAll();
        return R.ok(list);
    }

    /**
     * 获取标签详情
     */
    @Operation(summary = "获取标签详情")
    @GetMapping("/{id}")
    public R<TagVO> getDetail(@PathVariable Long id) {
        TagVO tag = tagService.getDetail(id);
        return R.ok(tag);
    }

    /**
     * 创建标签
     */
    @Operation(summary = "创建标签")
    @PostMapping
    public R<TagVO> create(@Valid @RequestBody TagDTO dto) {
        TagVO tag = tagService.create(dto);
        return R.ok(tag, "创建成功");
    }

    /**
     * 更新标签
     */
    @Operation(summary = "更新标签")
    @PutMapping("/{id}")
    public R<TagVO> update(@PathVariable Long id, @Valid @RequestBody TagDTO dto) {
        TagVO tag = tagService.update(id, dto);
        return R.ok(tag, "更新成功");
    }

    /**
     * 删除标签
     */
    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return R.ok(null, "删除成功");
    }
}
