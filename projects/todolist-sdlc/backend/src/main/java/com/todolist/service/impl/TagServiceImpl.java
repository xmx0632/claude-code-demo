package com.todolist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todolist.common.exception.BusinessException;
import com.todolist.dto.TagDTO;
import com.todolist.dto.TagQueryDTO;
import com.todolist.entity.Tag;
import com.todolist.mapper.TagMapper;
import com.todolist.mapper.TodoTagMapper;
import com.todolist.service.TagService;
import com.todolist.util.SecurityUtils;
import com.todolist.vo.TagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务标签服务实现
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final TodoTagMapper todoTagMapper;

    @Override
    public IPage<TagVO> pageList(TagQueryDTO query) {
        Long userId = SecurityUtils.getCurrentUserId();

        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getUserId, userId)
               .like(query.getName() != null, Tag::getName, query.getName())
               .orderByDesc(Tag::getCreatedAt);

        Page<Tag> page = new Page<>(query.getPage(), query.getSize());
        IPage<Tag> tagPage = this.page(page, wrapper);

        return tagPage.convert(this::convertToVO);
    }

    @Override
    public List<TagVO> listAll() {
        Long userId = SecurityUtils.getCurrentUserId();
        List<Tag> tags = baseMapper.selectListWithTaskCount(userId);
        return tags.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public TagVO getDetail(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();

        Tag tag = this.getOne(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getId, id)
                .eq(Tag::getUserId, userId));

        if (tag == null) {
            throw new BusinessException("标签不存在");
        }

        return convertToVO(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TagVO create(TagDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();

        // 验证标签名唯一性
        if (!isNameUnique(userId, dto.getName(), null)) {
            throw new BusinessException("标签名称已存在");
        }

        Tag tag = new Tag();
        BeanUtils.copyProperties(dto, tag);
        tag.setUserId(userId);
        tag.setColor(dto.getColor() != null ? dto.getColor() : "#999999");

        this.save(tag);
        return convertToVO(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TagVO update(Long id, TagDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();

        Tag tag = this.getOne(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getId, id)
                .eq(Tag::getUserId, userId));

        if (tag == null) {
            throw new BusinessException("标签不存在");
        }

        // 验证标签名唯一性
        if (!isNameUnique(userId, dto.getName(), id)) {
            throw new BusinessException("标签名称已存在");
        }

        BeanUtils.copyProperties(dto, tag);
        if (dto.getColor() != null) {
            tag.setColor(dto.getColor());
        }

        this.updateById(tag);
        return convertToVO(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();

        Tag tag = this.getOne(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getId, id)
                .eq(Tag::getUserId, userId));

        if (tag == null) {
            throw new BusinessException("标签不存在");
        }

        // 删除标签（关联关系会由数据库级联删除）
        this.removeById(id);
    }

    @Override
    public boolean isNameUnique(Long userId, String name, Long excludeId) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getUserId, userId)
               .eq(Tag::getName, name);

        if (excludeId != null) {
            wrapper.ne(Tag::getId, excludeId);
        }

        return this.count(wrapper) == 0;
    }

    /**
     * 转换为VO
     */
    private TagVO convertToVO(Tag tag) {
        TagVO vo = new TagVO();
        BeanUtils.copyProperties(tag, vo);

        // 查询任务数量
        if (tag.getId() != null) {
            Integer count = todoTagMapper.selectCount(
                    new LambdaQueryWrapper<>()
                            .eq(com.todolist.entity.TodoTag::getTagId, tag.getId())
            );
            vo.setTaskCount(count);
        }

        return vo;
    }
}
