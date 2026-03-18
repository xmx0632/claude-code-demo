package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 图书数据访问接口
 *
 * @author Claude Code
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * 根据标题模糊查询
     */
    Page<Book> findByTitleContaining(String title, Pageable pageable);

    /**
     * 根据作者模糊查询
     */
    Page<Book> findByAuthorContaining(String author, Pageable pageable);

    /**
     * 根据ISBN查询
     */
    Book findByIsbn(String isbn);

    /**
     * 检查ISBN是否存在
     */
    boolean existsByIsbn(String isbn);

    /**
     * 检查ISBN是否存在（排除指定ID）
     */
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    /**
     * 获取低库存图书
     */
    @Query("SELECT b FROM Book b WHERE b.stockQuantity <= b.minStock")
    List<Book> findLowStockBooks();

    /**
     * 搜索图书（多条件）
     */
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR b.title LIKE %:title%) AND " +
           "(:author IS NULL OR b.author LIKE %:author%) AND " +
           "(:categoryId IS NULL OR b.categoryId = :categoryId)")
    Page<Book> searchBooks(@Param("title") String title,
                          @Param("author") String author,
                          @Param("categoryId") Long categoryId,
                          Pageable pageable);
}
