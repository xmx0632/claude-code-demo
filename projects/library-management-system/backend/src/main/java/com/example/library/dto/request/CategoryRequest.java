package com.example.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 分类请求DTO
 *
 * @author Claude Code
 */
@Data
public class CategoryRequest {

    @NotBlank(message = "分类名称不能为空")
    private String name;

    private String description;
}
