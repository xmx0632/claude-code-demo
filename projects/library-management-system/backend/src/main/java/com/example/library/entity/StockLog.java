package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 库存日志实体类
 *
 * @author Claude Code
 */
@Data
@Entity
@Table(name = "stock_logs")
public class StockLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(nullable = false, length = 10)
    private String type;  // IN - 入库, OUT - 出库

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "before_quantity", nullable = false)
    private Integer beforeQuantity;

    @Column(name = "after_quantity", nullable = false)
    private Integer afterQuantity;

    @Column(length = 200)
    private String remark;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 关联图书信息（不映射为实体关系，用于查询返回）
    @Transient
    private String bookTitle;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
