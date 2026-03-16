---
name: api-doc
description: 解析 Spring Boot Controller 并生成 API 文档。需要文档化接口时使用。
allowed-tools: ["Read", "Grep", "Glob", "Bash"]
---

# API 文档生成器

自动解析 Spring Boot Controller 类，生成 Markdown 格式的 API 文档。

## 功能特性

- 自动识别 @RestController 和 @Controller
- 解析 @RequestMapping 系列注解
- 提取请求参数和响应格式
- 支持 Swagger 注解
- 生成接口列表和分组

## 使用方法

### 生成所有 API 文档

```
/api-doc
```

### 生成特定 Controller

```
/api-doc UserController
```

### 输出到文件

```
/api-doc --output=docs/api.md
```

## 文档结构

```markdown
# API 文档

## 用户管理

### 用户列表
- **接口**: GET /api/user/list
- **描述**: 查询用户列表
- **请求参数**:
  - pageNum: 页码
  - pageSize: 每页数量
- **响应**:
\`\`\`json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [...]
}
\`\`\`
```

## 支持的注解

- `@RestController`
- `@Controller`
- `@RequestMapping`
- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`
- `@RequestParam`
- `@PathVariable`
- `@RequestBody`
- `@ApiOperation` (Swagger)
- `@ApiImplicitParam` (Swagger)

## 注意事项

- 确保项目已编译（类文件存在）
- Controller 类使用标准注解
- 复杂的泛型类型可能无法正确解析

## 导出格式

默认生成 Markdown 格式，也可以选择：
- 纯文本
- JSON 格式
- Swagger YAML

## 后续操作

1. 检查生成的文档完整性
2. 补充业务逻辑说明
3. 添加示例代码
4. 分享给前端团队
