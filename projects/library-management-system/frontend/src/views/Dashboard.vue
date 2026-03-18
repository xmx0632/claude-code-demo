<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#409EFF"><Books /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ summary.totalBooks || 0 }}</div>
              <div class="stat-label">图书总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#67C23A"><Menu /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ summary.totalCategories || 0 }}</div>
              <div class="stat-label">分类数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#E6A23C"><Warning /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ summary.lowStockCount || 0 }}</div>
              <div class="stat-label">低库存预警</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#909399"><Document /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ summary.totalStockLogs || 0 }}</div>
              <div class="stat-label">操作记录</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="low-stock-card" v-if="lowStockBooks.length > 0">
      <template #header>
        <div class="card-header">
          <span>低库存预警</span>
          <el-tag type="warning">共 {{ lowStockBooks.length }} 本</el-tag>
        </div>
      </template>
      <el-table :data="lowStockBooks" style="width: 100%">
        <el-table-column prop="title" label="书名" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="stockQuantity" label="当前库存" width="100">
          <template #default="{ row }">
            <el-tag type="warning">{{ row.stockQuantity }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" label="最小库存" width="100" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleStockIn(row)">
              入库
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSummary, getLowStockBooks } from '@/api/stats'
import { stockIn } from '@/api/book'
import { ElMessage, ElMessageBox } from 'element-plus'

const summary = ref({})
const lowStockBooks = ref([])

const loadSummary = async () => {
  summary.value = await getSummary()
}

const loadLowStockBooks = async () => {
  lowStockBooks.value = await getLowStockBooks()
}

const handleStockIn = (row) => {
  ElMessageBox.prompt('请输入入库数量', '入库操作', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^[1-9]\d*$/,
    inputErrorMessage: '请输入有效的数量'
  }).then(async ({ value }) => {
    await stockIn(row.id, { quantity: parseInt(value), remark: '低库存补货' })
    ElMessage.success('入库成功')
    loadSummary()
    loadLowStockBooks()
  }).catch(() => {})
}

onMounted(() => {
  loadSummary()
  loadLowStockBooks()
})
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  font-size: 48px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.low-stock-card {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
