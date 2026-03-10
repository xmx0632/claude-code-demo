package com.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * User Data Access Interface
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * Select user by username
     *
     * @param username Username
     * @return User entity
     */
    User selectByUsername(@Param("username") String username);

    /**
     * Check if username exists
     *
     * @param username Username
     * @return true if exists, false otherwise
     */
    boolean existsByUsername(@Param("username") String username);
}
