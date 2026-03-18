package com.example.library.service;

import com.example.library.dto.request.BookRequest;
import com.example.library.dto.request.StockOperationRequest;
import com.example.library.dto.response.BookResponse;
import com.example.library.dto.response.PageResponse;
import com.example.library.entity.Book;
import com.example.library.entity.StockLog;
import com.example.library.repository.BookRepository;
import com.example.library.repository.StockLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图书业务逻辑类
 *
 * @author Claude Code
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final StockLogRepository stockLogRepository;

    /**
     * 分页查询图书
     */
    public Map<String, Object> findAll(Integer page, Integer size, String sortBy, String sortOrder) {
        Sort sort = "desc".equalsIgnoreCase(sortOrder) ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Book> bookPage = bookRepository.findAll(pageable);

        List<BookResponse> records = bookPage.getContent().stream()
                .map(this::enrichWithCategoryName)
                .map(BookResponse::from)
                .collect(Collectors.toList());

        return Map.of(
                "records", records,
                "total", bookPage.getTotalElements(),
                "current", page,
                "size", size,
                "pages", bookPage.getTotalPages()
        );
    }

    /**
     * 搜索图书
     */
    public Map<String, Object> search(String title, String author, Long categoryId,
                                      Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        Page<Book> bookPage = bookRepository.searchBooks(title, author, categoryId, pageable);

        List<BookResponse> records = bookPage.getContent().stream()
                .map(this::enrichWithCategoryName)
                .map(BookResponse::from)
                .collect(Collectors.toList());

        return Map.of(
                "records", records,
                "total", bookPage.getTotalElements(),
                "current", page,
                "size", size,
                "pages", bookPage.getTotalPages()
        );
    }

    /**
     * 根据ID获取图书
     */
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图书不存在"));
        return BookResponse.from(enrichWithCategoryName(book));
    }

    /**
     * 创建图书
     */
    @Transactional
    public BookResponse create(BookRequest request) {
        // 检查ISBN是否已存在
        if (request.getIsbn() != null && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new RuntimeException("ISBN已存在");
        }

        Book book = new Book();
        copyFromRequest(book, request);

        Book saved = bookRepository.save(book);
        return BookResponse.from(saved);
    }

    /**
     * 更新图书
     */
    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图书不存在"));

        // 检查ISBN是否被其他图书使用
        if (request.getIsbn() != null &&
            bookRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
            throw new RuntimeException("ISBN已存在");
        }

        copyFromRequest(book, request);
        Book updated = bookRepository.save(book);
        return BookResponse.from(updated);
    }

    /**
     * 删除图书
     */
    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("图书不存在");
        }
        bookRepository.deleteById(id);
    }

    /**
     * 入库操作
     */
    @Transactional
    public void stockIn(Long id, StockOperationRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图书不存在"));

        int beforeQuantity = book.getStockQuantity();
        int afterQuantity = beforeQuantity + request.getQuantity();

        book.setStockQuantity(afterQuantity);
        bookRepository.save(book);

        // 记录库存日志
        StockLog log = new StockLog();
        log.setBookId(id);
        log.setType("IN");
        log.setQuantity(request.getQuantity());
        log.setBeforeQuantity(beforeQuantity);
        log.setAfterQuantity(afterQuantity);
        log.setRemark(request.getRemark());
        stockLogRepository.save(log);
    }

    /**
     * 出库操作
     */
    @Transactional
    public void stockOut(Long id, StockOperationRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图书不存在"));

        int beforeQuantity = book.getStockQuantity();
        if (beforeQuantity < request.getQuantity()) {
            throw new RuntimeException("库存不足，当前库存: " + beforeQuantity);
        }

        int afterQuantity = beforeQuantity - request.getQuantity();

        book.setStockQuantity(afterQuantity);
        bookRepository.save(book);

        // 记录库存日志
        StockLog log = new StockLog();
        log.setBookId(id);
        log.setType("OUT");
        log.setQuantity(request.getQuantity());
        log.setBeforeQuantity(beforeQuantity);
        log.setAfterQuantity(afterQuantity);
        log.setRemark(request.getRemark());
        stockLogRepository.save(log);
    }

    /**
     * 获取低库存图书
     */
    public List<BookResponse> getLowStockBooks() {
        return bookRepository.findLowStockBooks().stream()
                .map(this::enrichWithCategoryName)
                .map(BookResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 丰富分类名称（通过categoryRepository）
     */
    private Book enrichWithCategoryName(Book book) {
        // 暂时不做join，简化处理
        return book;
    }

    /**
     * 从请求复制属性
     */
    private void copyFromRequest(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCategoryId(request.getCategoryId());
        book.setPrice(request.getPrice());
        book.setPublisher(request.getPublisher());
        book.setPublishDate(request.getPublishDate());
        book.setDescription(request.getDescription());
        book.setStockQuantity(request.getStockQuantity());
        book.setMinStock(request.getMinStock());
    }
}
