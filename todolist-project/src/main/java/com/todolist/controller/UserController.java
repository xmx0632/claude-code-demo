package com.todolist.controller;

import com.todolist.common.response.R;
import com.todolist.dto.request.UpdatePasswordDTO;
import com.todolist.dto.response.UserVO;
import com.todolist.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User information management endpoints")
public class UserController {

    private final IUserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get User Profile")
    public R<UserVO> getProfile() {
        UserVO vo = userService.getCurrentUserProfile();
        return R.ok(vo);
    }

    @PutMapping("/password")
    @Operation(summary = "Update Password")
    public R<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto) {
        userService.updatePassword(dto);
        return R.ok("Password updated successfully, please login again");
    }
}
