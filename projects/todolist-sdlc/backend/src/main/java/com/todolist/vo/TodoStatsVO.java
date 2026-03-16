package com.todolist.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务统计VO
 *
 * @author Claude Code
 * @since 2026-03-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoStatsVO {

    /**
     * 全部任务数
     */
    private Long all;

    /**
     * 待办任务数
     */
    private Long pending;

    /**
     * 进行中任务数
     */
    private Long inProgress;

    /**
     * 已完成任务数
     */
    private Long completed;
}
