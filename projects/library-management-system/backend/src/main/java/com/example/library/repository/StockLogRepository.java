package com.example.library.repository;

import com.example.library.entity.StockLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 库存日志数据访问接口
 *
 * @author Claude Code
 */
@Repository
public interface StockLogRepository extends JpaRepository<StockLog, Long> {

    /**
     * 根据图书ID查询日志
     */
    Page<StockLog> findByBookIdOrderByCreatedAtDesc(Long bookId, Pageable pageable);

    /**
     * 根据操作类型查询日志
     */
    Page<StockLog> findByTypeOrderByCreatedAtDesc(String type, Pageable pageable);

    /**
     * 获取图书的最新日志
     */
    @Query("SELECT s FROM StockLog s WHERE s.bookId = :bookId ORDER BY s.createdAt DESC")
    Page<StockLog> findLatestByBookId(@Param("bookId") Long bookId, Pageable pageable);
}
