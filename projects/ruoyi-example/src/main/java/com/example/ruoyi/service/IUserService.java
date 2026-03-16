package com.example.ruoyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruoyi.domain.User;

import java.util.List;

/**
 * 用户服务接口
 *
 * 演示 MyBatis-Plus IService 使用
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User selectUserById(Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param userName 用户名
     * @return 用户信息
     */
    User selectUserByName(String userName);

    /**
     * 查询用户列表
     *
     * @param user 用户信息
     * @return 用户列表
     */
    List<User> selectUserList(User user);

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean insertUser(User user);

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean updateUser(User user);

    /**
     * 批量删除用户
     *
     * @param userIds 用户ID数组
     * @return 结果
     */
    boolean deleteUserByIds(Long[] userIds);

    /**
     * 根据部门ID查询用户列表
     *
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<User> selectUsersByDeptId(Long deptId);
}
