<template>
  <el-dialog
    :model-value="modelValue"
    :title="title"
    width="400px"
    @update:model-value="$emit('update:modelValue', $event)"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-form-item label="图书">
        <span>{{ book?.title }}</span>
      </el-form-item>
      <el-form-item label="当前库存">
        <el-tag :type="book?.lowStock ? 'warning' : 'success'">
          {{ book?.stockQuantity }}
        </el-tag>
      </el-form-item>
      <el-form-item label="操作数量" prop="quantity">
        <el-input-number
          v-model="form.quantity"
          :min="1"
          :max="type === 'OUT' ? book?.stockQuantity : 9999"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息（可选）"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:modelValue', false)">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: Boolean,
  book: Object,
  type: {
    type: String,
    default: 'IN'
  }
})

const emit = defineEmits(['update:modelValue', 'confirm'])

const formRef = ref(null)
const loading = ref(false)

const title = computed(() => {
  return props.type === 'IN' ? '入库操作' : '出库操作'
})

const form = reactive({
  quantity: 1,
  remark: ''
})

const rules = {
  quantity: [
    { required: true, message: '请输入操作数量', trigger: 'blur' },
    { type: 'number', min: 1, message: '数量必须大于0', trigger: 'blur' }
  ]
}

const handleConfirm = async () => {
  await formRef.value.validate()

  if (props.type === 'OUT' && form.quantity > props.book?.stockQuantity) {
    ElMessage.error('出库数量不能大于当前库存')
    return
  }

  loading.value = true
  try {
    await emit('confirm', {
      quantity: form.quantity,
      remark: form.remark
    })
    // 重置表单
    form.quantity = 1
    form.remark = ''
  } finally {
    loading.value = false
  }
}
</script>
