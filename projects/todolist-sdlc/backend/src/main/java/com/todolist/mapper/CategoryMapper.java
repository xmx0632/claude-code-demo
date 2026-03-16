package com.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类Mapper
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
