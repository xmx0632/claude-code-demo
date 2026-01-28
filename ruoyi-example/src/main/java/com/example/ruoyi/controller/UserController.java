package com.example.ruoyi.controller;

import com.example.ruoyi.domain.User;
import com.example.ruoyi.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 *
 * 演示 RESTful API 设计
 */
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 查询用户列表
     */
    @GetMapping("/list")
    public Map<String, Object> list(User user) {
        List<User> list = userService.selectUserList(user);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("rows", list);
        result.put("total", list.size());
        return result;
    }

    /**
     * 根据用户ID查询用户
     */
    @GetMapping("/{userId}")
    public Map<String, Object> getInfo(@PathVariable Long userId) {
        User user = userService.selectUserById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", user);
        return result;
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Map<String, Object> add(@RequestBody User user) {
        boolean success = userService.insertUser(user);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "操作成功" : "操作失败");
        return result;
    }

    /**
     * 修改用户
     */
    @PutMapping
    public Map<String, Object> edit(@RequestBody User user) {
        boolean success = userService.updateUser(user);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "操作成功" : "操作失败");
        return result;
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    public Map<String, Object> remove(@PathVariable Long[] userIds) {
        boolean success = userService.deleteUserByIds(userIds);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "操作成功" : "操作失败");
        return result;
    }
}
