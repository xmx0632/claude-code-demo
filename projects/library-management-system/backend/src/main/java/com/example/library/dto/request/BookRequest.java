package com.example.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 图书请求DTO
 *
 * @author Claude Code
 */
@Data
public class BookRequest {

    @NotBlank(message = "书名不能为空")
    private String title;

    @NotBlank(message = "作者不能为空")
    private String author;

    private String isbn;

    private Long categoryId;

    private BigDecimal price;

    private String publisher;

    private LocalDate publishDate;

    private String description;

    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能小于0")
    private Integer stockQuantity;

    @NotNull(message = "最小库存不能为空")
    @Min(value = 0, message = "最小库存不能小于0")
    private Integer minStock;
}
