package com.todolist.controller;

import com.todolist.common.response.R;
import com.todolist.dto.request.UpdatePasswordDTO;
import com.todolist.dto.response.UserVO;
import com.todolist.service.IUserService;
import javax.validation.Valid;
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
public class UserController {

    private final IUserService userService;

    @GetMapping("/profile")
    public R<UserVO> getProfile() {
        UserVO vo = userService.getCurrentUserProfile();
        return R.ok(vo);
    }

    @PutMapping("/password")
    public R<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto) {
        userService.updatePassword(dto);
        return R.<Void>ok("Password updated successfully, please login again", null);
    }
}
