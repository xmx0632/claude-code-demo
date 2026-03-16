package com.todolist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.todolist.dto.TagDTO;
import com.todolist.dto.TagQueryDTO;
import com.todolist.entity.Tag;
import com.todolist.vo.TagVO;

import java.util.List;

/**
 * 任务标签服务接口
 *
 * @author Claude Code
 * @since 2026-03-16
 */
public interface TagService extends IService<Tag> {

    /**
     * 分页查询标签列表
     *
     * @param query 查询条件
     * @return 标签分页列表
     */
    IPage<TagVO> pageList(TagQueryDTO query);

    /**
     * 查询用户的所有标签
     *
     * @return 标签列表
     */
    List<TagVO> listAll();

    /**
     * 获取标签详情
     *
     * @param id 标签ID
     * @return 标签详情
     */
    TagVO getDetail(Long id);

    /**
     * 创建标签
     *
     * @param dto 标签DTO
     * @return 标签VO
     */
    TagVO create(TagDTO dto);

    /**
     * 更新标签
     *
     * @param id  标签ID
     * @param dto 标签DTO
     * @return 标签VO
     */
    TagVO update(Long id, TagDTO dto);

    /**
     * 删除标签
     *
     * @param id 标签ID
     */
    void delete(Long id);

    /**
     * 检查标签名是否唯一
     *
     * @param userId 用户ID
     * @param name   标签名称
     * @param excludeId 排除的标签ID（更新时使用）
     * @return 是否唯一
     */
    boolean isNameUnique(Long userId, String name, Long excludeId);
}
