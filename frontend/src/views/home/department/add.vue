<!-- D:\maven_project\mas\frontend\src\views\home\department\add.vue -->
<template>
  <div class="add-department">
    <el-card :title="isEdit ? '编辑科室' : '添加科室'" class="department-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" class="department-form">
        <el-form-item label="科室名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入科室名称" />
        </el-form-item>
        <el-form-item label="科室描述" prop="description">
          <el-input v-model="form.description" placeholder="请输入科室描述" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit" :loading="loading">保存</el-button>
          <el-button @click="$router.push('/home/departments')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { departmentApi } from '@/api'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const isEdit = ref(false)

const form = reactive({
  name: '',
  description: ''
})

const rules = {
  name: [{ required: true, message: '请输入科室名称', trigger: 'blur' }]
}

const loadDepartment = async (id) => {
  try {
    const res = await departmentApi.getById(id)
    const data = res.data.data
    form.name = data.name
    form.description = data.description
  } catch (error) {
    console.error('加载科室信息失败:', error)
    ElMessage.error('加载科室信息失败')
  }
}

const submit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    if (isEdit.value) {
      await departmentApi.update(route.params.id, form)
      ElMessage.success('更新成功')
    } else {
      await departmentApi.create(form)
      ElMessage.success('创建成功')
    }
    router.push('/home/departments')
  } catch (error) {
    ElMessage.error('操作失败: ' + error.response?.data?.message)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (route.params.id) {
    isEdit.value = true
    loadDepartment(route.params.id)
  }
})
</script>

<style scoped>
.add-department {
  padding: 20px;
}

.department-card {
  max-width: 600px;
}

.department-form {
  margin-top: 20px;
}
</style>