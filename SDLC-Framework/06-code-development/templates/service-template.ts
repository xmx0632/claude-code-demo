/**
 * [服务名称] 服务模板
 *
 * 职责: [服务职责描述]
 */

import { Injectable, Logger } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';

// ============================================
// 类型定义
// ============================================

export interface {Entity}CreateInput {
  // TODO: 定义创建输入
}

export interface {Entity}UpdateInput {
  // TODO: 定义更新输入
}

export interface {Entity}QueryInput {
  page?: number;
  size?: number;
  keyword?: string;
}

// ============================================
// 服务实现
// ============================================

@Injectable()
export class {Entity}Service {
  private readonly logger = new Logger({Entity}Service.name);

  constructor(
    @InjectRepository({Entity})
    private readonly repository: Repository<{Entity}>,
  ) {}

  /**
   * 创建 {Entity}
   */
  async create(input: {Entity}CreateInput): Promise<{Entity}> {
    this.logger.log('Creating {entity}', { input });

    // TODO: 输入验证
    // TODO: 业务逻辑

    const entity = this.repository.create(input);
    return await this.repository.save(entity);
  }

  /**
   * 查询列表
   */
  async findAll(query: {Entity}QueryInput): Promise<{ list: {Entity}[]; total: number }> {
    const { page = 1, size = 20, keyword } = query;

    const qb = this.repository.createQueryBuilder('{entity}');

    if (keyword) {
      qb.andWhere('{entity}.name LIKE :keyword', { keyword: `%${keyword}%` });
    }

    qb.skip((page - 1) * size).take(size);

    const [list, total] = await qb.getManyAndCount();
    return { list, total };
  }

  /**
   * 查询单个
   */
  async findOne(id: string): Promise<{Entity} | null> {
    return await this.repository.findOne({ where: { id } });
  }

  /**
   * 更新
   */
  async update(id: string, input: {Entity}UpdateInput): Promise<{Entity}> {
    const entity = await this.findOne(id);
    if (!entity) {
      throw new Error('{Entity} not found');
    }

    Object.assign(entity, input);
    return await this.repository.save(entity);
  }

  /**
   * 删除
   */
  async remove(id: string): Promise<void> {
    await this.repository.softDelete(id);
  }
}
