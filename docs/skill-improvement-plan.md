# Skill 改进计划

> 基于 SDLC-Framework SKILL.md 与现有 `.claude/skills/` 对比分析

---

## 一、现状对比

### 1.1 SDLC-Framework 阶段 vs 现有 Skills 映射

| SDLC 阶段 | SKILL.md | 现有 Skill | 覆盖度 |
|-----------|----------|------------|--------|
| 01-需求分析 | requirements-analysis | ✅ sdlc-requirements-analysis | 完整 |
| 02-产品设计 | product-design | ⚠️ 无独立 skill | 部分(ui-ux-pro-max) |
| 03-架构设计 | architecture-design | ✅ sdlc-architecture-design | 完整 |
| 04-详细设计 | detailed-design | ✅ sdlc-detailed-design | 完整 |
| 05-数据库迁移 | database-migration | ✅ sdlc-flyway-migration | 完整 |
| 06-代码开发 | code-development | ✅ sdlc-code-development | 完整 |
| 07-单元测试 | unit-testing | ✅ sdlc-test-gen | 完整 |
| 08-集成测试 | integration-testing | ⚠️ 合并在 sdlc-testing | 部分 |
| 09-系统测试 | system-testing | ⚠️ 合并在 sdlc-testing | 部分 |
| 10-测试用例 | test-case-writing | ❌ 缺失 | 缺失 |
| 11-系统验收 | system-acceptance | ❌ 缺失 | 缺失 |
| 12-用户手册 | user-manual | ⚠️ 合并在 sdlc-documentation | 部分 |
| 13-运维手册 | operations-manual | ⚠️ 合并在 sdlc-documentation | 部分 |
| 14-部署指南 | deployment-instructions | ✅ sdlc-deployment | 完整 |
| 15-增量升级 | incremental-upgrade | ❌ 缺失 | 缺失 |

### 1.2 结构对比

| 维度 | SDLC-Framework SKILL.md | 现有 Skills | 差距 |
|------|-------------------------|-------------|------|
| **输入/输出定义** | ✅ 明确表格形式 | ⚠️ 部分有 | 需统一 |
| **触发命令** | ✅ 独立章节 | ✅ 有 | 一致 |
| **执行步骤** | ✅ 详细步骤 | ⚠️ 粗略 | 需细化 |
| **Guards 触发** | ✅ 表格定义 | ❌ 缺失 | **需补充** |
| **质量门禁** | ✅ Checklist 格式 | ⚠️ 部分有 | 需统一 |
| **代码模板** | ⚠️ TypeScript 为主 | ✅ Java/Spring | 可互补 |
| **相关文件** | ✅ 引用其他阶段 | ❌ 缺失 | **需补充** |
| **阶段依赖** | ❌ 未明确 | ❌ 缺失 | **需补充** |

---

## 二、可学习的改进点

### 2.1 结构标准化

**SDLC-Framework 标准结构：**
```markdown
# [Skill Name] Skill

> 阶段 X: [阶段名称]

---

## 触发命令
## 阶段目标
## 输入
## 输出
## 执行步骤
## 触发的 Guards
## 质量门禁
## 相关文件
```

**建议：** 所有 skill 统一采用此结构

### 2.2 Guards 机制

SDLC-Framework 定义了 Guards 触发条件：

| Guard | 触发场景 |
|-------|----------|
| Security Agent | 安全相关代码、认证授权 |
| Performance Agent | 查询优化、性能关键代码 |
| Infra Agent | 配置、部署相关 |

**建议：** 在现有 skills 中添加 Guards 触发条件定义

### 2.3 输入/输出表格化

SDLC-Framework 使用表格明确定义：

```markdown
## 输出

| 产出物 | 目录 | 说明 |
|--------|------|------|
| 单元测试代码 | tests/unit/ | Jest 测试文件 |
| 测试覆盖率报告 | coverage/ | 覆盖率 HTML |
```

**建议：** 统一使用表格格式

### 2.4 阶段依赖关系

SDLC-Framework 隐含了阶段依赖：
- 阶段 7 (单元测试) 依赖阶段 6 (代码开发)
- 阶段 8 (集成测试) 依赖阶段 7 (单元测试)

**建议：** 在每个 skill 中明确标注前置阶段和后续阶段

---

## 三、具体改进计划

### 3.1 高优先级 - 结构标准化

| 序号 | 改进项 | 涉及 Skills | 工作量 |
|------|--------|-------------|--------|
| 1 | 统一输入/输出表格格式 | 全部 | 2h |
| 2 | 添加 Guards 触发条件 | 全部 | 1h |
| 3 | 添加质量门禁 Checklist | 全部 | 1h |
| 4 | 添加相关文件引用 | 全部 | 1h |

### 3.2 中优先级 - 功能补充

| 序号 | 改进项 | 说明 | 工作量 |
|------|--------|------|--------|
| 5 | 新增 `sdlc-test-case` | 独立的测试用例编写 skill | 2h |
| 6 | 新增 `sdlc-system-acceptance` | 系统验收 skill | 2h |
| 7 | 新增 `sdlc-incremental-upgrade` | 增量升级 skill | 2h |
| 8 | 拆分 `sdlc-testing` | 分离单元/集成/系统测试 | 3h |

### 3.3 低优先级 - 增强完善

| 序号 | 改进项 | 说明 | 工作量 |
|------|--------|------|--------|
| 9 | 添加多语言模板 | 补充 TypeScript/Go 模板 | 4h |
| 10 | 添加阶段依赖图 | 可视化阶段关系 | 2h |
| 11 | 添加示例项目 | 每个阶段添加实际案例 | 4h |

---

## 四、测试相关 Skills 改进详解

### 4.1 现有 `sdlc-test-gen` vs SDLC-Framework `07-unit-testing`

| 维度 | SDLC-Framework | 现有 skill | 改进建议 |
|------|----------------|------------|----------|
| 触发命令 | `/test-gen <module>` | `/test-gen <module>` | ✅ 一致 |
| 输入定义 | 源代码、API 文档 | 无明确定义 | **需补充** |
| 输出定义 | tests/unit/, coverage/ | 无明确定义 | **需补充** |
| 执行步骤 | 4 步详细流程 | 无 | **需补充** |
| Guards | Security/Performance | 无 | **需补充** |
| 质量门禁 | 5 项 Checklist | 无 | **需补充** |
| 代码模板 | TypeScript/Jest | Java/JUnit | 可共存 |

### 4.2 建议的 `sdlc-test-gen` 改进结构

```markdown
---
name: sdlc-test-gen
description: 为 Service 层方法生成单元测试。补充测试用例时使用。
allowed-tools: ["Read", "Write", "Edit", "Grep", "Bash"]
user-invocable: true
---

# 单元测试生成器

> 阶段 7: 单元测试

## 触发命令

/test-gen <module_name>

## 阶段目标

为业务代码编写单元测试，确保代码质量和可维护性。

## 输入

| 产出物 | 来源阶段 | 说明 |
|--------|----------|------|
| 源代码 | 阶段 6 | Service/Controller 代码 |
| API 设计文档 | 阶段 4 | 接口规范 |

## 输出

| 产出物 | 目录 | 说明 |
|--------|------|------|
| 单元测试代码 | src/test/java/ | JUnit 测试文件 |
| 测试覆盖率报告 | target/site/jacoco/ | JaCoCo HTML 报告 |

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
mvn test
mvn test -Dtest=UserServiceTest
mvn jacoco:report
```

### 4. 代码审查
- 测试覆盖率检查
- 测试用例评审
- 边界条件确认

## 触发的 Guards

| Guard | 触发条件 |
|-------|----------|
| Security Agent | 涉及安全相关代码 |
| Performance Agent | 涉及性能关键代码 |

## 质量门禁

- [ ] 测试覆盖率 ≥ 80%
- [ ] 所有测试通过
- [ ] 无跳过的测试
- [ ] 边界条件已覆盖
- [ ] 异常处理已测试

## 代码模板

### Java/JUnit5 模板

[现有模板保留]

## 相关文件

- 依赖阶段: `/sdlc-code-development`
- 后续阶段: `/sdlc-testing` (集成测试)
- 模板目录: `SDLC-Framework/07-unit-testing/templates/`
```

---

## 五、实施时间表

| 阶段 | 内容 | 预计时间 |
|------|------|----------|
| Week 1 | 结构标准化（高优先级 1-4） | 5h |
| Week 2 | 功能补充（中优先级 5-8） | 9h |
| Week 3 | 增强完善（低优先级 9-11） | 10h |

---

## 六、总结

### 主要差距
1. **Guards 机制** - SDLC-Framework 定义了 Guards 触发，现有 skills 完全缺失
2. **阶段依赖** - 缺少明确的前置/后续阶段定义
3. **结构统一** - 输入/输出/质量门禁格式不统一
4. **缺失 Skills** - 测试用例编写、系统验收、增量升级

### 建议优先实施
1. 统一所有 skills 的结构格式
2. 添加 Guards 触发条件
3. 补充缺失的独立 skills
4. 添加阶段依赖关系
