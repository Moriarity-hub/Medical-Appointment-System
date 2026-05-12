<!-- D:\maven_project\mas\frontend\src\views\patient\profile.vue -->
<template>
  <div class="profile-page">
    <el-card title="个人资料" class="profile-card">
      <el-form :model="form" label-width="120px" class="profile-form">
        <el-form-item label="姓名">
          <el-input v-model="form.name" disabled />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard" disabled />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { patientApi } from '@/api'

const form = reactive({
  name: '',
  idCard: '',
  phone: '',
  email: '',
  address: ''
})

const loadProfile = async () => {
  const patientId = localStorage.getItem('relatedId')
  if (!patientId) return

  try {
    const res = await patientApi.getById(patientId)
    const data = res.data.data
    form.name = data.name || ''
    form.idCard = data.idCard || ''
    form.phone = data.phone || ''
    form.email = data.email || ''
    form.address = data.address || ''
  } catch (error) {
    console.error('加载个人资料失败:', error)
  }
}

const save = async () => {
  const patientId = localStorage.getItem('relatedId')
  if (!patientId) return

  try {
    await patientApi.update(patientId, {
      email: form.email,
      address: form.address
    })
    ElMessage.success('修改成功')
  } catch (error) {
    ElMessage.error('修改失败')
    console.error(error)
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.profile-page {
  padding: 10px;
}

.profile-card {
  max-width: 600px;
}

.profile-form {
  margin-top: 20px;
}
</style>