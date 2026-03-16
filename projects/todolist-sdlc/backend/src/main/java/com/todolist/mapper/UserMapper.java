package com.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
