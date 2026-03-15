package com.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.Todo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务Mapper
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Mapper
public interface TodoMapper extends BaseMapper<Todo> {
}
