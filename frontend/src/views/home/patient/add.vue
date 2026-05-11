<!-- D:\maven_project\mas\frontend\src\views\home\patient\add.vue -->
<template>
  <div class="add-patient">
    <el-card :title="isEdit ? '编辑患者' : '添加患者'" class="patient-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" class="patient-form">
        <el-form-item label="患者姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请设置密码（不设置则默认为手机号后6位）" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit" :loading="loading">保存</el-button>
          <el-button @click="$router.push('/home/patients')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { patientApi } from '@/api'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const isEdit = ref(false)

const form = reactive({
  name: '',
  idCard: '',
  phone: '',
  email: '',
  address: '',
  password: ''
})

const rules = {
  name: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const loadPatient = async (id) => {
  try {
    const res = await patientApi.getById(id)
    const data = res.data.data
    form.name = data.name
    form.idCard = data.idCard
    form.phone = data.phone
    form.email = data.email
    form.address = data.address
    form.password = ''
  } catch (error) {
    console.error('加载患者信息失败:', error)
    ElMessage.error('加载患者信息失败')
  }
}

const submit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    if (isEdit.value) {
      await patientApi.update(route.params.id, form)
      ElMessage.success('更新成功')
    } else {
      await patientApi.create(form)
      ElMessage.success('创建成功')
    }
    router.push('/home/patients')
  } catch (error) {
    ElMessage.error('操作失败: ' + error.response?.data?.message)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (route.params.id) {
    isEdit.value = true
    loadPatient(route.params.id)
  }
})
</script>

<style scoped>
.add-patient {
  padding: 20px;
}

.patient-card {
  max-width: 600px;
}

.patient-form {
  margin-top: 20px;
}
</style>