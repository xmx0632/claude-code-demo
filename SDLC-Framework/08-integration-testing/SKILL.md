# Integration Testing Skill

> 阶段 8: 集成测试

---

## 触发命令

```bash
/integration-test
```

---

## 阶段目标

测试模块间的集成，验证接口契约和数据流。

---

## 输入

- 源代码 (阶段 6 产出)
- API 设计文档 (阶段 4 产出)
- 单元测试 (阶段 7 产出)

---

## 输出

| 产出物 | 目录 | 说明 |
|--------|------|------|
| 集成测试代码 | tests/integration/ | API 集成测试 |
| 测试数据 | tests/fixtures/ | 测试数据集 |
| 测试报告 | reports/integration/ | 测试执行报告 |

---

## 执行步骤

### 1. 准备测试环境

```bash
# 启动测试数据库
docker-compose -f docker-compose.test.yml up -d

# 运行迁移
npm run migration:test
```

### 2. 编写集成测试

- 测试 API 端点
- 测试数据库操作
- 测试外部服务集成

### 3. 执行测试

```bash
# 运行集成测试
npm run test:integration

# 并行执行
npm run test:integration -- --parallel
```

### 4. 生成报告

- 收集测试结果
- 分析失败原因
- 生成测试报告

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Security Agent | API 认证授权测试 |
| Performance Agent | 接口响应时间测试 |

---

## 测试模板

```typescript
describe('API Integration: /api/v1/users', () => {
  beforeAll(async () => {
    // 设置测试环境
    await setupTestDatabase();
    await seedTestData();
  });

  afterAll(async () => {
    // 清理测试环境
    await cleanupTestDatabase();
  });

  describe('POST /api/v1/users', () => {
    it('should create user successfully', async () => {
      const response = await request(app)
        .post('/api/v1/users')
        .set('Authorization', `Bearer ${testToken}`)
        .send({
          name: 'Test User',
          email: 'test@example.com',
        });

      expect(response.status).toBe(201);
      expect(response.body.data.id).toBeDefined();
    });

    it('should return 401 without token', async () => {
      const response = await request(app)
        .post('/api/v1/users')
        .send({ name: 'Test User' });

      expect(response.status).toBe(401);
    });
  });

  describe('GET /api/v1/users', () => {
    it('should return paginated user list', async () => {
      const response = await request(app)
        .get('/api/v1/users?page=1&size=10')
        .set('Authorization', `Bearer ${testToken}`);

      expect(response.status).toBe(200);
      expect(response.body.data.list).toBeInstanceOf(Array);
      expect(response.body.data.total).toBeGreaterThanOrEqual(0);
    });
  });
});
```

---

## 质量门禁

- [ ] 所有 API 端点已测试
- [ ] 认证授权已验证
- [ ] 数据库事务已验证
- [ ] 错误处理已验证
- [ ] 测试通过率 100%

---

## 相关文件

- 模板目录: `08-integration-testing/templates/`
- 角色定义: `roles/qa-engineer.md`
- 工作流: `workflows/full-sdlc-workflow.md`
