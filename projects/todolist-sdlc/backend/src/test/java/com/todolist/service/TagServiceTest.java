package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todolist.common.exception.BusinessException;
import com.todolist.dto.TagDTO;
import com.todolist.dto.TagQueryDTO;
import com.todolist.entity.Tag;
import com.todolist.entity.TodoTag;
import com.todolist.mapper.TagMapper;
import com.todolist.mapper.TodoTagMapper;
import com.todolist.service.impl.TagServiceImpl;
import com.todolist.vo.TagVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TagService 单元测试
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("任务标签服务 - 单元测试")
class TagServiceTest {

    @Mock
    private TagMapper tagMapper;

    @Mock
    private TodoTagMapper todoTagMapper;

    private TagServiceImpl tagService;

    private Tag testTag;
    private TagDTO testTagDTO;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl();
        ReflectionTestUtils.setField(tagService, "baseMapper", tagMapper);
        ReflectionTestUtils.setField(tagService, "todoTagMapper", todoTagMapper);

        // 设置安全上下文
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(1L, null, Collections.emptyList())
        );

        // 创建测试数据
        testTag = new Tag();
        testTag.setId(1L);
        testTag.setUserId(1L);
        testTag.setName("工作");
        testTag.setColor("#FF6B6B");
        testTag.setCreatedAt(LocalDateTime.now());
        testTag.setUpdatedAt(LocalDateTime.now());

        testTagDTO = new TagDTO();
        testTagDTO.setName("工作");
        testTagDTO.setColor("#FF6B6B");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("分页查询标签列表 - 成功")
    void pageList_Success() {
        // Arrange
        TagQueryDTO query = new TagQueryDTO();
        query.setPage(1);
        query.setSize(10);

        Page<Tag> page = new Page<>(1, 10);
        page.setRecords(Collections.singletonList(testTag));
        page.setTotal(1);

        when(tagMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);
        when(todoTagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(5);

        // Act
        IPage<TagVO> result = tagService.pageList(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals("工作", result.getRecords().get(0).getName());
        assertEquals("#FF6B6B", result.getRecords().get(0).getColor());
    }

    @Test
    @DisplayName("分页查询标签列表 - 按名称搜索")
    void pageList_WithName() {
        // Arrange
        TagQueryDTO query = new TagQueryDTO();
        query.setPage(1);
        query.setSize(10);
        query.setName("工作");

        Page<Tag> page = new Page<>(1, 10);
        page.setRecords(Collections.singletonList(testTag));
        page.setTotal(1);

        when(tagMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);
        when(todoTagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(5);

        // Act
        IPage<TagVO> result = tagService.pageList(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    @DisplayName("查询所有标签 - 成功")
    void listAll_Success() {
        // Arrange
        List<Tag> tags = Arrays.asList(testTag);
        when(tagMapper.selectListWithTaskCount(1L)).thenReturn(tags);

        // Act
        List<TagVO> result = tagService.listAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("工作", result.get(0).getName());
        verify(tagMapper, times(1)).selectListWithTaskCount(1L);
    }

    @Test
    @DisplayName("获取标签详情 - 成功")
    void getDetail_Success() {
        // Arrange
        when(tagMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class)))
                .thenReturn(testTag);
        when(todoTagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(5);

        // Act
        TagVO result = tagService.getDetail(1L);

        // Assert
        assertNotNull(result);
        assertEquals("工作", result.getName());
        assertEquals("#FF6B6B", result.getColor());
        assertEquals(5, result.getTaskCount());
    }

    @Test
    @DisplayName("获取标签详情 - 标签不存在")
    void getDetail_NotFound() {
        // Arrange
        when(tagMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class)))
                .thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            tagService.getDetail(999L);
        });

        assertEquals("标签不存在", exception.getMessage());
    }

    @Test
    @DisplayName("创建标签 - 成功")
    void create_Success() {
        // Arrange
        when(tagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(tagMapper.insert(any(Tag.class))).thenReturn(1);

        // Act
        TagVO result = tagService.create(testTagDTO);

        // Assert
        assertNotNull(result);
        assertEquals("工作", result.getName());
        assertEquals("#FF6B6B", result.getColor());
        verify(tagMapper, times(1)).insert(any(Tag.class));
    }

    @Test
    @DisplayName("创建标签 - 使用默认颜色")
    void create_WithDefaultColor() {
        // Arrange
        TagDTO dto = new TagDTO();
        dto.setName("学习");
        // 不设置颜色

        when(tagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(tagMapper.insert(any(Tag.class))).thenReturn(1);

        // Act
        TagVO result = tagService.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals("学习", result.getName());
    }

    @Test
    @DisplayName("创建标签 - 名称已存在")
    void create_DuplicateName() {
        // Arrange
        when(tagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            tagService.create(testTagDTO);
        });

        assertEquals("标签名称已存在", exception.getMessage());
        verify(tagMapper, never()).insert(any(Tag.class));
    }

    @Test
    @DisplayName("更新标签 - 成功")
    void update_Success() {
        // Arrange
        TagDTO dto = new TagDTO();
        dto.setName("工作事务");
        dto.setColor("#FF5555");

        when(tagMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class)))
                .thenReturn(testTag);
        when(tagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(tagMapper.updateById(any(Tag.class))).thenReturn(1);

        // Act
        TagVO result = tagService.update(1L, dto);

        // Assert
        assertNotNull(result);
        verify(tagMapper, times(1)).updateById(any(Tag.class));
    }

    @Test
    @DisplayName("更新标签 - 标签不存在")
    void update_NotFound() {
        // Arrange
        when(tagMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class)))
                .thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            tagService.update(999L, testTagDTO);
        });

        assertEquals("标签不存在", exception.getMessage());
    }

    @Test
    @DisplayName("更新标签 - 名称与其他标签重复")
    void update_DuplicateName() {
        // Arrange
        TagDTO dto = new TagDTO();
        dto.setName("个人");
        dto.setColor("#4ECDC4");

        when(tagMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class)))
                .thenReturn(testTag);
        when(tagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            tagService.update(1L, dto);
        });

        assertEquals("标签名称已存在", exception.getMessage());
    }

    @Test
    @DisplayName("删除标签 - 成功")
    void delete_Success() {
        // Arrange
        when(tagMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class)))
                .thenReturn(testTag);
        when(tagMapper.deleteById(1L)).thenReturn(1);

        // Act
        tagService.delete(1L);

        // Assert
        verify(tagMapper, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("删除标签 - 标签不存在")
    void delete_NotFound() {
        // Arrange
        when(tagMapper.selectOne(any(LambdaQueryWrapper.class), any(Boolean.class)))
                .thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            tagService.delete(999L);
        });

        assertEquals("标签不存在", exception.getMessage());
    }

    @Test
    @DisplayName("检查标签名唯一性 - 唯一")
    void isNameUnique_True() {
        // Arrange
        when(tagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        // Act
        boolean result = tagService.isNameUnique(1L, "工作", null);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("检查标签名唯一性 - 不唯一")
    void isNameUnique_False() {
        // Arrange
        when(tagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // Act
        boolean result = tagService.isNameUnique(1L, "工作", null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("检查标签名唯一性 - 排除自身")
    void isNameUnique_ExcludeSelf() {
        // Arrange
        when(tagMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        // Act
        boolean result = tagService.isNameUnique(1L, "工作", 1L);

        // Assert
        assertTrue(result);
    }
}
