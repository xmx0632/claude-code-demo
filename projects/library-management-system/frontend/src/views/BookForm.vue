<template>
  <div class="book-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑图书' : '新增图书' }}</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        v-loading="loading"
      >
        <el-form-item label="书名" prop="title">
          <el-input v-model="form.title" placeholder="请输入书名" />
        </el-form-item>

        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者" />
        </el-form-item>

        <el-form-item label="ISBN" prop="isbn">
          <el-input v-model="form.isbn" placeholder="请输入ISBN" />
        </el-form-item>

        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>

        <el-form-item label="出版社">
          <el-input v-model="form.publisher" placeholder="请输入出版社" />
        </el-form-item>

        <el-form-item label="出版日期">
          <el-date-picker
            v-model="form.publishDate"
            type="date"
            placeholder="请选择出版日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="库存数量" prop="stockQuantity">
          <el-input-number v-model="form.stockQuantity" :min="0" style="width: 100%" />
        </el-form-item>

        <el-form-item label="最小库存" prop="minStock">
          <el-input-number v-model="form.minStock" :min="0" style="width: 100%" />
        </el-form-item>

        <el-form-item label="图书描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入图书描述"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getBook, createBook, updateBook } from '@/api/book'
import { useLibraryStore } from '@/stores/library'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const libraryStore = useLibraryStore()

const formRef = ref(null)
const loading = ref(false)
const submitting = ref(false)
const categories = ref([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  title: '',
  author: '',
  isbn: '',
  categoryId: null,
  price: null,
  publisher: '',
  publishDate: '',
  stockQuantity: 0,
  minStock: 5,
  description: ''
})

const rules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  stockQuantity: [
    { required: true, message: '请输入库存数量', trigger: 'blur' },
    { type: 'number', min: 0, message: '库存数量不能小于0', trigger: 'blur' }
  ],
  minStock: [
    { required: true, message: '请输入最小库存', trigger: 'blur' },
    { type: 'number', min: 0, message: '最小库存不能小于0', trigger: 'blur' }
  ]
}

// 加载图书详情
const loadBook = async () => {
  loading.value = true
  try {
    const data = await getBook(route.params.id)
    Object.assign(form, data)
  } catch (error) {
    ElMessage.error('加载图书详情失败')
  } finally {
    loading.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value.validate()

  submitting.value = true
  try {
    if (isEdit.value) {
      await updateBook(route.params.id, form)
      ElMessage.success('更新成功')
    } else {
      await createBook(form)
      ElMessage.success('创建成功')
    }
    handleBack()
  } catch (error) {
    ElMessage.error(error.message || '提交失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const handleReset = () => {
  formRef.value.resetFields()
}

// 返回列表
const handleBack = () => {
  router.back()
}

onMounted(async () => {
  await libraryStore.loadCategories()
  categories.value = libraryStore.categories

  if (isEdit.value) {
    loadBook()
  }
})
</script>

<style scoped>
.book-form {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
