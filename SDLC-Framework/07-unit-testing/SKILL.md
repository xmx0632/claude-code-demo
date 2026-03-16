# Unit Testing Skill

> 阶段 7: 单元测试

---

## 触发命令

```bash
/test-gen <module_name>
```

---

## 阶段目标

为业务代码编写单元测试，确保代码质量和可维护性。

---

## 输入

- 源代码 (阶段 6 产出)
- API 设计文档 (阶段 4 产出)

---

## 输出

| 产出物 | 目录 | 说明 |
|--------|------|------|
| 单元测试代码 | tests/unit/ | Jest/Vitest 测试文件 |
| 测试覆盖率报告 | coverage/ | 覆盖率 HTML 报告 |

---

## 执行步骤

### 1. 分析测试范围

- 识别需要测试的函数/类
- 确定测试优先级
- 规划测试用例

### 2. 编写测试

- 编写正常流程测试
- 编写边界条件测试
- 编写异常处理测试

### 3. 执行测试

```bash
# 运行所有测试
npm test

# 运行特定模块测试
npm test -- module_name

# 生成覆盖率报告
npm test -- --coverage
```

### 4. 代码审查

- 测试覆盖率检查
- 测试用例评审
- 边界条件确认

---

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Security Agent | 涉及安全相关代码 |
| Performance Agent | 涉及性能关键代码 |

---

## 测试模板

```typescript
describe('{ModuleName}', () => {
  describe('{functionName}', () => {
    it('should return expected result for valid input', () => {
      // Arrange
      const input = 'valid_input';
      const expected = 'expected_output';

      // Act
      const result = functionName(input);

      // Assert
      expect(result).toBe(expected);
    });

    it('should throw error for invalid input', () => {
      // Arrange
      const input = 'invalid_input';

      // Act & Assert
      expect(() => functionName(input)).toThrow(Error);
    });

    it('should handle edge case: empty input', () => {
      // Arrange
      const input = '';

      // Act
      const result = functionName(input);

      // Assert
      expect(result).toBe('');
    });
  });
});
```

---

## 质量门禁

- [ ] 测试覆盖率 ≥ 80%
- [ ] 所有测试通过
- [ ] 无跳过的测试
- [ ] 边界条件已覆盖
- [ ] 异常处理已测试

---

## 相关文件

- 模板目录: `07-unit-testing/templates/`
- 角色定义: `roles/backend-developer.md`, `roles/qa-engineer.md`
- 工作流: `workflows/full-sdlc-workflow.md`
