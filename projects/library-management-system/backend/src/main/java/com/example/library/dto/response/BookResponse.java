package com.example.library.dto.response;

import com.example.library.entity.Book;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 图书响应DTO
 *
 * @author Claude Code
 */
@Data
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Long categoryId;
    private String categoryName;
    private BigDecimal price;
    private String publisher;
    private LocalDate publishDate;
    private String description;
    private Integer stockQuantity;
    private Integer minStock;
    private Boolean lowStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 从实体转换
     */
    public static BookResponse from(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setCategoryId(book.getCategoryId());
        response.setCategoryName(book.getCategoryName());
        response.setPrice(book.getPrice());
        response.setPublisher(book.getPublisher());
        response.setPublishDate(book.getPublishDate());
        response.setDescription(book.getDescription());
        response.setStockQuantity(book.getStockQuantity());
        response.setMinStock(book.getMinStock());
        response.setLowStock(book.isLowStock());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }
}
