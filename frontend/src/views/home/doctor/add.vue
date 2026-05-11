<!-- D:\maven_project\mas\frontend\src\views\home\doctor\add.vue -->
<template>
  <div class="add-doctor">
    <el-card :title="isEdit ? '编辑医生' : '添加医生'" class="doctor-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" class="doctor-form">
        <el-form-item label="医生姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="form.title" placeholder="请输入职称" />
        </el-form-item>
        <el-form-item label="专业领域" prop="specialty">
          <el-input v-model="form.specialty" placeholder="请输入专业领域" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="所属科室" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="请选择科室">
            <el-option
                v-for="dept in departments"
                :key="dept.id"
                :label="dept.name"
                :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit" :loading="loading">保存</el-button>
          <el-button @click="$router.push('/home/doctors')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { doctorApi, departmentApi } from '@/api'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const isEdit = ref(false)
const departments = ref([])

const form = reactive({
  name: '',
  title: '',
  specialty: '',
  phone: '',
  email: '',
  departmentId: ''
})

const rules = {
  name: [{ required: true, message: '请输入医生姓名', trigger: 'blur' }],
  title: [{ required: true, message: '请输入职称', trigger: 'blur' }],
  departmentId: [{ required: true, message: '请选择科室', trigger: 'change' }]
}

const loadDepartments = async () => {
  try {
    const res = await departmentApi.getAll()
    departments.value = res.data.data
  } catch (error) {
    console.error('加载科室列表失败:', error)
    ElMessage.error('加载科室列表失败')
  }
}

const loadDoctor = async (id) => {
  try {
    const res = await doctorApi.getById(id)
    const data = res.data.data
    form.name = data.name
    form.title = data.title
    form.specialty = data.specialty
    form.phone = data.phone
    form.email = data.email
    form.departmentId = data.department?.id || ''
  } catch (error) {
    console.error('加载医生信息失败:', error)
    ElMessage.error('加载医生信息失败')
  }
}

const submit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    if (isEdit.value) {
      await doctorApi.update(route.params.id, form)
      ElMessage.success('更新成功')
    } else {
      await doctorApi.create(form)
      ElMessage.success('创建成功')
    }
    router.push('/home/doctors')
  } catch (error) {
    ElMessage.error('操作失败: ' + error.response?.data?.message)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDepartments()
  if (route.params.id) {
    isEdit.value = true
    loadDoctor(route.params.id)
  }
})
</script>

<style scoped>
.add-doctor {
  padding: 20px;
}

.doctor-card {
  max-width: 600px;
}

.doctor-form {
  margin-top: 20px;
}
</style>