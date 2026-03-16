# ADR-003: 标签筛选使用 AND 逻辑而非 OR 逻辑

## 状态

已接受

## 日期

2026-03-16

## 背景

用户可以同时选择多个标签进行筛选，需要决定使用 AND 逻辑（同时拥有所有标签）还是 OR 逻辑（拥有任一标签）。

## 决策

**采用 AND 逻辑**

## 理由

| 用户体验 | AND 逻辑 | OR 逻辑 | 选择 |
|----------|----------|---------|------|
| 精确度 | 高（缩小范围） | 低（扩大范围） | ✅ AND |
| 习惯 | 符合直觉（交集） | 可能误操作 | ✅ AND |
| 应用场景 | 查找特定任务 | 浏览所有相关 | ✅ AND |
| 用户反馈 | 更常用 | 较少使用 | ✅ AND |

### 示例

| 选择标签 | AND 逻辑结果 | OR 逻辑结果 |
|----------|--------------|-------------|
| 工作 + 重要 | 有"工作"且"重要"的任务 | 有"工作"或有"重要"的任务 |
| 3个标签 | 同时有这3个标签的任务 | 有这3个标签中任一的任务 |

## 后果

### 正面

- 筛选结果更精确，用户体验更好
- 符合主流产品习惯（Notion、TickTick）
- 查询效率更高（索引友好）

### 负面

- 选择多标签后可能没有结果
- 需要给用户清晰的提示

## 实现方案

### SQL 实现

```sql
SELECT t.* FROM t_todo t
WHERE t.user_id = #{userId}
  AND EXISTS (
    SELECT 1 FROM t_todo_tag tt
    WHERE tt.todo_id = t.id
      AND tt.tag_id IN (1, 2, 3)
  HAVING COUNT(*) = 3
)
```

### MyBatis-Plus

```java
LambdaQueryWrapper<Todo> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(Todo::getUserId, userId);
wrapper.and(w -> {
    for (Long tagId : tagIds) {
        w.exists("SELECT 1 FROM t_todo_tag tt WHERE tt.todo_id = t.id AND tt.tag_id = {0}", tagId);
    }
});
```

## 前端交互

```javascript
// 选中标签时
function toggleTag(tagId) {
  if (selectedTags.includes(tagId)) {
    selectedTags = selectedTags.filter(id => id !== tagId);
  } else {
    selectedTags.push(tagId);
  }
  // 使用 AND 逻辑查询
  fetchTodos({ tagIds: selectedTags });
}
```

## 用户提示

```html
<div class="filter-hint">
  已选择 <strong>3</strong> 个标签，
  显示同时包含这些标签的任务
</div>
```

## 替代方案

### 方案 A: OR 逻辑

- **优点**：结果更多，不容易为空
- **缺点**：筛选太宽泛，不够精确
- **拒绝理由**：用户调研显示更常用 AND 逻辑

### 方案 B: AND/OR 切换

- **优点**：灵活度高
- **缺点**：交互复杂，认知负担重
- **拒绝理由**：增加复杂度，非必须功能

## 参考

- [Notion 标签筛选](https://www.google.com/search?q=notion+tag+filter)
- [TickTick 标签筛选](https://www.google.com/search?q=ticktick+tag+filter)
