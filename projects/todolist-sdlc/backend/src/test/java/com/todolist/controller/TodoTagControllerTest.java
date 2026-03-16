package com.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.dto.TodoTagsDTO;
import com.todolist.service.TodoTagService;
import com.todolist.vo.TagVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TodoTagController 集成测试
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@WebMvcTest(TodoTagController.class)
@ActiveProfiles("test")
@DisplayName("任务标签关联控制器 - 集成测试")
class TodoTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoTagService todoTagService;

    @Test
    @WithMockUser(username = "1")
    @DisplayName("为任务添加标签 - 成功")
    void addTags_Success() throws Exception {
        // Arrange
        TodoTagsDTO dto = new TodoTagsDTO();
        dto.setTagIds(Arrays.asList(1L, 2L));

        TagVO tagVO1 = new TagVO();
        tagVO1.setId(1L);
        tagVO1.setName("工作");
        tagVO1.setColor("#FF6B6B");

        TagVO tagVO2 = new TagVO();
        tagVO2.setId(2L);
        tagVO2.setName("重要");
        tagVO2.setColor("#45B7D1");

        when(todoTagService.addTags(eq(1L), any(TodoTagsDTO.class)))
                .thenReturn(Arrays.asList(tagVO1, tagVO2));

        // Act & Assert
        mockMvc.perform(post("/api/todos/1/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("添加成功"))
                .andExpect(jsonPath("$.data[0].name").value("工作"))
                .andExpect(jsonPath("$.data[1].name").value("重要"));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("为任务添加标签 - 验证失败：标签列表为空")
    void addTags_EmptyTagIds() throws Exception {
        // Arrange
        TodoTagsDTO dto = new TodoTagsDTO();
        dto.setTagIds(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(post("/api/todos/1/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("为任务添加标签 - 验证失败：标签列表为null")
    void addTags_NullTagIds() throws Exception {
        // Arrange
        TodoTagsDTO dto = new TodoTagsDTO();
        // tagIds 为 null

        // Act & Assert
        mockMvc.perform(post("/api/todos/1/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("移除任务标签 - 成功")
    void removeTag_Success() throws Exception {
        // Arrange
        org.mockito.Mockito.doNothing().when(todoTagService).removeTag(1L, 1L);

        // Act & Assert
        mockMvc.perform(delete("/api/todos/1/tags/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("移除成功"));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("查询任务的所有标签 - 成功")
    void getTagsByTodoId_Success() throws Exception {
        // Arrange
        TagVO tagVO1 = new TagVO();
        tagVO1.setId(1L);
        tagVO1.setName("工作");
        tagVO1.setColor("#FF6B6B");

        TagVO tagVO2 = new TagVO();
        tagVO2.setId(2L);
        tagVO2.setName("重要");
        tagVO2.setColor("#45B7D1");

        when(todoTagService.getTagsByTodoId(1L))
                .thenReturn(Arrays.asList(tagVO1, tagVO2));

        // Act & Assert
        mockMvc.perform(get("/api/todos/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("工作"))
                .andExpect(jsonPath("$.data[1].name").value("重要"));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("查询任务的所有标签 - 无标签")
    void getTagsByTodoId_NoTags() throws Exception {
        // Arrange
        when(todoTagService.getTagsByTodoId(1L))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/todos/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("批量更新任务标签 - 成功")
    void updateTags_Success() throws Exception {
        // Arrange
        TodoTagsDTO dto = new TodoTagsDTO();
        dto.setTagIds(Arrays.asList(1L, 3L, 5L));

        TagVO tagVO1 = new TagVO();
        tagVO1.setId(1L);
        tagVO1.setName("工作");
        tagVO1.setColor("#FF6B6B");

        TagVO tagVO2 = new TagVO();
        tagVO2.setId(3L);
        tagVO2.setName("重要");
        tagVO2.setColor("#45B7D1");

        TagVO tagVO3 = new TagVO();
        tagVO3.setId(5L);
        tagVO3.setName("紧急");
        tagVO3.setColor("#EF4444");

        when(todoTagService.updateTags(eq(1L), any(TodoTagsDTO.class)))
                .thenReturn(Arrays.asList(tagVO1, tagVO2, tagVO3));

        // Act & Assert
        mockMvc.perform(put("/api/todos/1/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("更新成功"))
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].name").value("工作"))
                .andExpect(jsonPath("$.data[1].name").value("重要"))
                .andExpect(jsonPath("$.data[2].name").value("紧急"));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("批量更新任务标签 - 清空所有标签")
    void updateTags_ClearAll() throws Exception {
        // Arrange
        TodoTagsDTO dto = new TodoTagsDTO();
        dto.setTagIds(Collections.emptyList());

        when(todoTagService.updateTags(eq(1L), any(TodoTagsDTO.class)))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(put("/api/todos/1/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("更新成功"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("未登录访问 - 返回401")
    void withoutAuth_Returns401() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/todos/1/tags"))
                .andExpect(status().isUnauthorized());
    }
}
