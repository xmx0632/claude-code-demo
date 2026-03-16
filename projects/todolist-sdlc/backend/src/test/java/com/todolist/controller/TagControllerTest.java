package com.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.dto.TagDTO;
import com.todolist.dto.TagQueryDTO;
import com.todolist.entity.Tag;
import com.todolist.mapper.TagMapper;
import com.todolist.mapper.TodoTagMapper;
import com.todolist.service.TagService;
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
 * TagController 集成测试
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@WebMvcTest(TagController.class)
@ActiveProfiles("test")
@DisplayName("任务标签控制器 - 集成测试")
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagService tagService;

    @MockBean
    private TagMapper tagMapper;

    @MockBean
    private TodoTagMapper todoTagMapper;

    @Test
    @WithMockUser(username = "1")
    @DisplayName("分页查询标签列表 - 成功")
    void pageList_Success() throws Exception {
        // Arrange
        when(tagService.pageList(any(TagQueryDTO.class)))
                .thenReturn(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10));

        // Act & Assert
        mockMvc.perform(get("/api/tags")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("查询所有标签 - 成功")
    void listAll_Success() throws Exception {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("工作");
        tag.setColor("#FF6B6B");

        com.todolist.vo.TagVO tagVO = new com.todolist.vo.TagVO();
        tagVO.setId(1L);
        tagVO.setName("工作");
        tagVO.setColor("#FF6B6B");
        tagVO.setTaskCount(5);

        when(tagService.listAll()).thenReturn(Arrays.asList(tagVO));

        // Act & Assert
        mockMvc.perform(get("/api/tags/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("工作"))
                .andExpect(jsonPath("$.data[0].color").value("#FF6B6B"))
                .andExpect(jsonPath("$.data[0].taskCount").value(5));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("查询所有标签 - 空列表")
    void listAll_Empty() throws Exception {
        // Arrange
        when(tagService.listAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/tags/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("获取标签详情 - 成功")
    void getDetail_Success() throws Exception {
        // Arrange
        com.todolist.vo.TagVO tagVO = new com.todolist.vo.TagVO();
        tagVO.setId(1L);
        tagVO.setName("工作");
        tagVO.setColor("#FF6B6B");
        tagVO.setTaskCount(5);

        when(tagService.getDetail(1L)).thenReturn(tagVO);

        // Act & Assert
        mockMvc.perform(get("/api/tags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("工作"))
                .andExpect(jsonPath("$.data.color").value("#FF6B6B"))
                .andExpect(jsonPath("$.data.taskCount").value(5));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("创建标签 - 成功")
    void create_Success() throws Exception {
        // Arrange
        TagDTO dto = new TagDTO();
        dto.setName("学习");
        dto.setColor("#BB8FCE");

        com.todolist.vo.TagVO tagVO = new com.todolist.vo.TagVO();
        tagVO.setId(1L);
        tagVO.setName("学习");
        tagVO.setColor("#BB8FCE");

        when(tagService.create(any(TagDTO.class))).thenReturn(tagVO);

        // Act & Assert
        mockMvc.perform(post("/api/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("创建成功"))
                .andExpect(jsonPath("$.data.name").value("学习"))
                .andExpect(jsonPath("$.data.color").value("#BB8FCE"));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("创建标签 - 验证失败：名称为空")
    void create_EmptyName() throws Exception {
        // Arrange
        TagDTO dto = new TagDTO();
        dto.setName("");
        dto.setColor("#FF6B6B");

        // Act & Assert
        mockMvc.perform(post("/api/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("创建标签 - 验证失败：颜色格式错误")
    void create_InvalidColor() throws Exception {
        // Arrange
        TagDTO dto = new TagDTO();
        dto.setName("学习");
        dto.setColor("invalid");

        // Act & Assert
        mockMvc.perform(post("/api/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("创建标签 - 验证失败：名称过长")
    void create_NameTooLong() throws Exception {
        // Arrange
        TagDTO dto = new TagDTO();
        dto.setName("这是一个非常非常长的标签名称超过了二十个字符的限制");
        dto.setColor("#FF6B6B");

        // Act & Assert
        mockMvc.perform(post("/api/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("更新标签 - 成功")
    void update_Success() throws Exception {
        // Arrange
        TagDTO dto = new TagDTO();
        dto.setName("工作事务");
        dto.setColor("#FF5555");

        com.todolist.vo.TagVO tagVO = new com.todolist.vo.TagVO();
        tagVO.setId(1L);
        tagVO.setName("工作事务");
        tagVO.setColor("#FF5555");

        when(tagService.update(eq(1L), any(TagDTO.class))).thenReturn(tagVO);

        // Act & Assert
        mockMvc.perform(put("/api/tags/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("更新成功"))
                .andExpect(jsonPath("$.data.name").value("工作事务"));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName("删除标签 - 成功")
    void delete_Success() throws Exception {
        // Arrange
        org.mockito.Mockito.doNothing().when(tagService).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/tags/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("删除成功"));
    }

    @Test
    @DisplayName("未登录访问 - 返回401")
    void withoutAuth_Returns401() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isUnauthorized());
    }
}
