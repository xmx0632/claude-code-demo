package com.example.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruoyi.domain.User;
import com.example.ruoyi.mapper.UserMapper;
import com.example.ruoyi.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 用户服务实现类
 *
 * 演示 MyBatis-Plus ServiceImpl 使用
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;

    @Override
    public User selectUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User selectUserByName(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, userName);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public List<User> selectUserList(User user) {
        LambdaQueryWrapper<User> wrapper = buildQueryWrapper(user);
        return userMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(User user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        return userMapper.updateById(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserByIds(Long[] userIds) {
        return userMapper.deleteBatchIds(Arrays.asList(userIds)) > 0;
    }

    @Override
    public List<User> selectUsersByDeptId(Long deptId) {
        return userMapper.selectUsersByDeptId(deptId);
    }

    /**
     * 构建查询条件
     *
     * @param user 用户信息
     * @return 查询包装器
     */
    private LambdaQueryWrapper<User> buildQueryWrapper(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 用户名模糊查询
        wrapper.like(StringUtils.hasText(user.getUserName()),
                     User::getUserName, user.getUserName());

        // 昵称模糊查询
        wrapper.like(StringUtils.hasText(user.getNickName()),
                     User::getNickName, user.getNickName());

        // 邮箱模糊查询
        wrapper.like(StringUtils.hasText(user.getEmail()),
                     User::getEmail, user.getEmail());

        // 部门ID精确查询
        wrapper.eq(user.getDeptId() != null,
                   User::getDeptId, user.getDeptId());

        // 状态精确查询
        wrapper.eq(StringUtils.hasText(user.getStatus()),
                   User::getStatus, user.getStatus());

        // 按创建时间降序排序
        wrapper.orderByDesc(User::getCreateTime);

        return wrapper;
    }
}
