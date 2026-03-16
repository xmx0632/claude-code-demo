package com.todolist.service;

import com.todolist.dto.request.UpdatePasswordDTO;
import com.todolist.dto.request.UserLoginDTO;
import com.todolist.dto.request.UserRegisterDTO;
import com.todolist.dto.response.AuthResponseVO;
import com.todolist.dto.response.UserVO;

/**
 * User Service Interface
 *
 * @author TodoList Team
 * @since 1.0.0
 */
public interface IUserService {

    /**
     * User registration
     *
     * @param dto Registration request
     * @return Authentication response
     */
    AuthResponseVO register(UserRegisterDTO dto);

    /**
     * User login
     *
     * @param dto Login request
     * @return Authentication response
     */
    AuthResponseVO login(UserLoginDTO dto);

    /**
     * Get current user profile
     *
     * @return User information
     */
    UserVO getCurrentUserProfile();

    /**
     * Update password
     *
     * @param dto Password update request
     */
    void updatePassword(UpdatePasswordDTO dto);

    /**
     * Get user by username
     *
     * @param username Username
     * @return User information
     */
    UserVO getByUsername(String username);
}
