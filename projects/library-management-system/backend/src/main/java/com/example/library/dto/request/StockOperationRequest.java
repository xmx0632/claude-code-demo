package com.example.library.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

/**
 * 库存操作请求DTO
 *
 * @author Claude Code
 */
@Data
public class StockOperationRequest {

    @NotNull(message = "操作数量不能为空")
    @Min(value = 1, message = "操作数量必须大于0")
    private Integer quantity;

    private String remark;
}
