<!-- D:\maven_project\mas\frontend\src\views\doctor\profile.vue -->
<template>
  <div class="profile-page">
    <el-card>
      <h3 class="card-title">个人资料</h3>

      <el-form :model="profileForm" label-width="120px" class="profile-form">
        <el-form-item label="姓名">
          <el-input v-model="profileForm.name" disabled />
        </el-form-item>
        <el-form-item label="职称">
          <el-input v-model="profileForm.title" disabled />
        </el-form-item>
        <el-form-item label="专业领域">
          <el-input v-model="profileForm.specialty" disabled />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="profileForm.phone" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="profileForm.email" disabled />
        </el-form-item>
        <el-form-item label="所属科室">
          <el-input v-model="profileForm.department" disabled />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-input v-model="profileForm.createdAt" disabled />
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { doctorApi } from '@/api'

const profileForm = ref({
  name: '',
  title: '',
  specialty: '',
  phone: '',
  email: '',
  department: '',
  createdAt: ''
})

const loadProfile = async () => {
  const doctorId = localStorage.getItem('relatedId')
  if (!doctorId) return

  try {
    const res = await doctorApi.getById(doctorId)
    const doctor = res.data.data

    profileForm.value = {
      name: doctor.name || '',
      title: doctor.title || '',
      specialty: doctor.specialty || '',
      phone: doctor.phone || '',
      email: doctor.email || '',
      department: doctor.department?.name || '',
      createdAt: doctor.createdAt || ''
    }
  } catch (error) {
    ElMessage.error('加载个人资料失败')
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

.card-title {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 18px;
}

.profile-form {
  max-width: 500px;
}

.profile-form :deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>