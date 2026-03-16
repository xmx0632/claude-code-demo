package com.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 任务标签Mapper
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 查询用户的标签列表（带任务数量）
     *
     * @param userId 用户ID
     * @return 标签列表
     */
    @Select("SELECT t.*, " +
            "(SELECT COUNT(*) FROM t_todo_tag tt WHERE tt.tag_id = t.id) as task_count " +
            "FROM t_tag t " +
            "WHERE t.user_id = #{userId} " +
            "ORDER BY t.created_at DESC")
    List<Tag> selectListWithTaskCount(@Param("userId") Long userId);

    /**
     * 根据任务ID查询标签列表
     *
     * @param todoId 任务ID
     * @return 标签列表
     */
    @Select("SELECT t.* FROM t_tag t " +
            "INNER JOIN t_todo_tag tt ON t.id = tt.tag_id " +
            "WHERE tt.todo_id = #{todoId}")
    List<Tag> selectByTodoId(@Param("todoId") Long todoId);

    /**
     * 根据标签ID列表查询任务ID列表
     *
     * @param tagIds 标签ID列表
     * @return 任务ID列表
     */
    @Select("<script>" +
            "SELECT DISTINCT todo_id FROM t_todo_tag " +
            "WHERE tag_id IN " +
            "<foreach collection='tagIds' item='tagId' open='(' separator=',' close=')'>" +
            "#{tagId}" +
            "</foreach>" +
            "</script>")
    List<Long> selectTodoIdsByTagIds(@Param("tagIds") List<Long> tagIds);
}
