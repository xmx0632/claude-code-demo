<template>
  <div class="book-list">
    <el-card>
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="书名">
          <el-input v-model="searchForm.title" placeholder="请输入书名" clearable />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="searchForm.author" placeholder="请输入作者" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="toolbar">
        <el-button type="primary" @click="handleCreate">
          <el-icon><Plus /></el-icon>
          新增图书
        </el-button>
      </div>

      <!-- 图书表格 -->
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="书名" min-width="200" />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="isbn" label="ISBN" width="140" />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="price" label="价格" width="80">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stockQuantity" label="库存" width="80">
          <template #default="{ row }">
            <el-tag :type="row.lowStock ? 'warning' : 'success'">
              {{ row.stockQuantity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleStockIn(row)">
              入库
            </el-button>
            <el-button type="warning" link size="small" @click="handleStockOut(row)">
              出库
            </el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        class="pagination"
      />
    </el-card>

    <!-- 库存操作对话框 -->
    <StockDialog
      v-model="stockDialogVisible"
      :book="currentBook"
      :type="stockType"
      @confirm="handleStockConfirm"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getBooks, searchBooks, deleteBook, stockIn, stockOut } from '@/api/book'
import { useLibraryStore } from '@/stores/library'
import { ElMessage, ElMessageBox } from 'element-plus'
import StockDialog from '@/components/StockDialog.vue'

const router = useRouter()
const libraryStore = useLibraryStore()

const loading = ref(false)
const tableData = ref([])
const categories = ref([])

const searchForm = reactive({
  title: '',
  author: '',
  categoryId: null
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const stockDialogVisible = ref(false)
const stockType = ref('IN')
const currentBook = ref(null)

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.current,
      size: pagination.size,
      sortBy: 'id',
      sortOrder: 'desc'
    }

    let result
    if (searchForm.title || searchForm.author || searchForm.categoryId) {
      result = await searchBooks({
        ...searchForm,
        ...params
      })
    } else {
      result = await getBooks(params)
    }

    tableData.value = result.records
    pagination.total = result.total
    pagination.pages = result.pages
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置
const handleReset = () => {
  searchForm.title = ''
  searchForm.author = ''
  searchForm.categoryId = null
  pagination.current = 1
  loadData()
}

// 新增
const handleCreate = () => {
  router.push('/books/create')
}

// 编辑
const handleEdit = (row) => {
  router.push(`/books/${row.id}/edit`)
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除《${row.title}》吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteBook(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

// 入库
const handleStockIn = (row) => {
  stockType.value = 'IN'
  currentBook.value = row
  stockDialogVisible.value = true
}

// 出库
const handleStockOut = (row) => {
  stockType.value = 'OUT'
  currentBook.value = row
  stockDialogVisible.value = true
}

// 确认库存操作
const handleStockConfirm = async (data) => {
  try {
    if (stockType.value === 'IN') {
      await stockIn(currentBook.value.id, data)
      ElMessage.success('入库成功')
    } else {
      await stockOut(currentBook.value.id, data)
      ElMessage.success('出库成功')
    }
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

onMounted(async () => {
  await libraryStore.loadCategories()
  categories.value = libraryStore.categories
  loadData()
})
</script>

<style scoped>
.book-list {
  height: 100%;
}

.search-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 16px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
