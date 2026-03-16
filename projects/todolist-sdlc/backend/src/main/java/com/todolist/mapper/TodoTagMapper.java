package com.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.TodoTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 任务标签关联Mapper
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Mapper
public interface TodoTagMapper extends BaseMapper<TodoTag> {

    /**
     * 查询任务的标签ID列表
     *
     * @param todoId 任务ID
     * @return 标签ID列表
     */
    @Select("SELECT tag_id FROM t_todo_tag WHERE todo_id = #{todoId}")
    List<Long> selectTagIdsByTodoId(@Param("todoId") Long todoId);

    /**
     * 批量查询多个任务的标签ID列表
     *
     * @param todoIds 任务ID列表
     * @return 标签ID列表
     */
    @Select("<script>" +
            "SELECT todo_id, tag_id FROM t_todo_tag " +
            "WHERE todo_id IN " +
            "<foreach collection='todoIds' item='todoId' open='(' separator=',' close=')'>" +
            "#{todoId}" +
            "</foreach>" +
            "</script>")
    List<TodoTag> selectByTodoIds(@Param("todoIds") List<Long> todoIds);

    /**
     * 删除任务的所有标签
     *
     * @param todoId 任务ID
     * @return 删除数量
     */
    int deleteByTodoId(@Param("todoId") Long todoId);
}
