<template>
  <div class="tag-manage-container">
    <div class="header">
      <h2>标签管理</h2>
      <el-button type="primary" icon="Plus" @click="showAddDialog">
        新建标签
      </el-button>
    </div>

    <!-- 标签列表 -->
    <div class="tag-list" v-loading="loading">
      <el-empty v-if="tagList.length === 0" description="暂无标签" />
      <div v-for="tag in tagList" :key="tag.id" class="tag-item">
        <div class="tag-color" :style="{ backgroundColor: tag.color }"></div>
        <div class="tag-info">
          <div class="tag-name">{{ tag.name }}</div>
          <div class="tag-count">{{ tag.taskCount || 0 }} 个任务</div>
        </div>
        <div class="tag-actions">
          <el-button link type="primary" @click="handleEdit(tag)">
            <el-icon><Edit /></el-icon>
          </el-button>
          <el-popconfirm title="确定删除?" @confirm="handleDelete(tag.id)">
            <template #reference>
              <el-button link type="danger">
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-popconfirm>
        </div>
      </div>
    </div>

    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑标签' : '新建标签'"
      width="400px"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入标签名称" maxlength="20" />
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-color-picker v-model="formData.color" />
          <el-input
            v-model="formData.color"
            placeholder="#RRGGBB"
            style="width: 120px; margin-left: 8px"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { tagApi } from '@/api/tag'

const loading = ref(false)
const tagList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const editId = ref(null)
const formRef = ref()

const formData = reactive({
  name: '',
  color: '#999999'
})

const formRules = {
  name: [
    { required: true, message: '请输入标签名称', trigger: 'blur' },
    { min: 1, max: 20, message: '长度为1-20字符', trigger: 'blur' }
  ],
  color: [
    { required: true, message: '请选择颜色', trigger: 'blur' },
    { pattern: /^#[0-9A-Fa-f]{6}$/, message: '颜色格式不正确', trigger: 'blur' }
  ]
}

onMounted(() => {
  fetchTagList()
})

async function fetchTagList() {
  loading.value = true
  try {
    const res = await tagApi.getAll()
    tagList.value = res.data || []
  } catch (error) {
    ElMessage.error('获取标签列表失败')
  } finally {
    loading.value = false
  }
}

function showAddDialog() {
  isEdit.value = false
  editId.value = null
  Object.assign(formData, {
    name: '',
    color: '#999999'
  })
  dialogVisible.value = true
}

function handleEdit(tag) {
  isEdit.value = true
  editId.value = tag.id
  Object.assign(formData, {
    name: tag.name,
    color: tag.color
  })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (isEdit.value) {
      await tagApi.update(editId.value, formData)
      ElMessage.success('更新成功')
    } else {
      await tagApi.create(formData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchTagList()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}

async function handleDelete(id) {
  try {
    await tagApi.delete(id)
    ElMessage.success('删除成功')
    fetchTagList()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.tag-manage-container {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header h2 {
  margin: 0;
  font-size: 20px;
}

.tag-list {
  min-height: 200px;
}

.tag-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
  transition: background 0.2s;
}

.tag-item:hover {
  background: #f9f9f9;
}

.tag-color {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  margin-right: 16px;
  flex-shrink: 0;
}

.tag-info {
  flex: 1;
}

.tag-name {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 4px;
}

.tag-count {
  font-size: 12px;
  color: #999;
}

.tag-actions {
  display: flex;
  gap: 8px;
}
</style>
