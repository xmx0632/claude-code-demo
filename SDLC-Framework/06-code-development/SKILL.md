# Code Development Skill

> 阶段 6: 代码开发

---

## 触发命令

```bash
/code-gen <module_name>
/ruoyi-crud <table_name>
```

---

## 阶段目标

实现业务逻辑代码，包括前端和后端。

---

## 输入

- 详细设计文档 (阶段 4 产出)
- 数据库迁移脚本 (阶段 5 产出)
- 项目宪法 (guidance/CONSTITUTION.md)

---

## 输出

| 产出物 | 目录 | 说明 |
|--------|------|------|
| 后端代码 | src/backend/ | API、Service、DAO |
| 前端代码 | src/frontend/ | 组件、页面、样式 |
| 单元测试 | tests/unit/ | 测试代码 |
| 代码文档 | docs/api/ | API 文档 |

---

## 执行步骤

### 1. 环境准备

- 拉取最新代码
- 安装依赖
- 配置开发环境

### 2. 后端开发

- 实现 API 接口
- 编写业务逻辑
- 实现数据访问层

### 3. 前端开发

- 创建 UI 组件
- 实现页面逻辑
- 对接后端 API

### 4. 单元测试

- 编写测试用例
- 执行测试
- 确保覆盖率

### 5. 代码提交

- 代码格式化
- 静态检查
- 提交代码

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Security Agent | 所有代码变更 |
| Performance Agent | 涉及查询、循环 |
| Infra Agent | 涉及配置、部署 |

---

## 代码规范

### 后端 (TypeScript)

```typescript
// 使用 TypeScript 类型
interface User {
  id: string;
  name: string;
  email: string;
}

// 输入验证
const validateUser = (user: unknown): User => {
  return UserSchema.parse(user);
};

// 错误处理
try {
  await service.createUser(user);
} catch (error) {
  logger.error('创建用户失败', { error, userId: user.id });
  throw new BusinessError('创建用户失败');
}
```

### 前端 (React)

```typescript
// 组件定义
export const UserList: React.FC<UserListProps> = ({ users }) => {
  return (
    <div className="user-list">
      {users.map(user => (
        <UserCard key={user.id} user={user} />
      ))}
    </div>
  );
};
```

---

## 质量门禁

- [ ] 代码格式化通过
- [ ] 静态检查无错误
- [ ] 单元测试通过
- [ ] 覆盖率 ≥ 80%
- [ ] 代码审查通过
- [ ] 无安全漏洞

---

## 相关文件

- 模板目录: `06-code-development/templates/`
- 角色定义: `roles/backend-developer.md`
- 工作流: `workflows/full-sdlc-workflow.md`
- Guards: `guards/security.md`, `guards/performance.md`
