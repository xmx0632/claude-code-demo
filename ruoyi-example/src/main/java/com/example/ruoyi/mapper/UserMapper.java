package com.example.ruoyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ruoyi.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据访问层
 *
 * 演示 MyBatis-Plus BaseMapper 使用
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据部门ID查询用户列表
     *
     * @param deptId 部门ID
     * @return 用户列表
     */
    @Select("SELECT * FROM sys_user WHERE dept_id = #{deptId} AND deleted = 0")
    List<User> selectUsersByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据状态查询用户数量
     *
     * @param status 用户状态
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM sys_user WHERE status = #{status} AND deleted = 0")
    int countByStatus(@Param("status") String status);
}
