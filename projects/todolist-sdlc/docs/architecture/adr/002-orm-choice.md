# ADR-002: 使用 MyBatis-Plus 而非 JPA/Hibernate

## 状态

已接受

## 日期

2026-03-16

## 背景

TodoList 项目使用 Spring Boot，需要选择 ORM 框架：MyBatis-Plus 或 JPA/Hibernate。

## 决策

**采用 MyBatis-Plus**

## 理由

| 考虑因素 | MyBatis-Plus | JPA/Hibernate | 选择 |
|----------|--------------|--------------|------|
| 学习曲线 | 低（类似 MyBatis） | 高（概念多） | ✅ MyBatis-Plus |
| SQL 控制 | 完全控制 | 抽象层隐藏 | ✅ MyBatis-Plus |
| 性能优化 | 容易优化 | 需要深入理解 | ✅ MyBatis-Plus |
| 复杂查询 | 灵活 | Criteria API 复杂 | ✅ MyBatis-Plus |
| 代码生成 | 内置代码生成 | 需要额外工具 | ✅ MyBatis-Plus |
| 中文文档 | 丰富 | 较少 | ✅ MyBatis-Plus |

## 后果

### 正面

- CRUD 操作零代码
- SQL 可读可维护
- 性能问题容易定位
- 国内社区活跃

### 负面

- 需要手写关联查询
- 没有 JPA 的标准化优势

## 替代方案

### 方案 A: Spring Data JPA

- **优点**：标准 JPA、Repository 模式、方法名查询
- **缺点**：性能调优复杂、N+1 问题隐蔽
- **拒绝理由**：团队更熟悉 MyBatis，需要 SQL 细粒度控制

## 代码示例

### TagMapper

```java
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    @Select("SELECT * FROM t_tag WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Tag> selectByUserId(@Param("userId") Long userId);
}
```

### 标签筛选查询

```xml
<select id="selectByTagIds" resultType="Todo">
    SELECT t.* FROM t_todo t
    WHERE t.user_id = #{userId}
      AND EXISTS (
        SELECT 1 FROM t_todo_tag tt
        WHERE tt.todo_id = t.id
          AND tt.tag_id IN
        <foreach collection="tagIds" item="tagId" open="(" close=")" separator=",">
          #{tagId}
        </foreach>
      )
    ORDER BY t.created_at DESC
</select>
```

## 参考

- [MyBatis-Plus 官方文档](https://baomidou.com)
