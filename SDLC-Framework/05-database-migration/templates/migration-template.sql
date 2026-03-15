-- 迁移脚本模板
-- 版本: V{version}__{operation}_{table}.sql
-- 描述: [迁移描述]
-- 作者: [作者]
-- 日期: [日期]

-- ============================================
-- 正向迁移
-- ============================================

-- 创建表
CREATE TABLE IF NOT EXISTS {table_name} (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,

    -- 业务字段
    -- TODO: 添加业务字段

    -- 索引字段
    tenant_id VARCHAR(64) NOT NULL
);

-- 创建索引
CREATE INDEX idx_{table_name}_tenant_id ON {table_name}(tenant_id);
CREATE INDEX idx_{table_name}_created_at ON {table_name}(created_at);

-- 创建更新时间触发器
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_{table_name}_updated_at
    BEFORE UPDATE ON {table_name}
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 添加注释
COMMENT ON TABLE {table_name} IS '[表描述]';
-- COMMENT ON COLUMN {table_name}.field IS '[字段描述]';
