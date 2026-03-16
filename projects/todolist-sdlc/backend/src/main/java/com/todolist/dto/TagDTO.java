package com.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 标签创建/更新DTO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
public class TagDTO {

    /**
     * 标签ID（更新时需要）
     */
    private Long id;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    @Size(min = 1, max = 20, message = "标签名称长度为1-20字符")
    private String name;

    /**
     * 标签颜色（HEX格式）
     */
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "颜色格式不正确，应为#RRGGBB格式")
    private String color;
}
