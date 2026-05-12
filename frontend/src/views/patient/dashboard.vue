<!-- D:\maven_project\mas\frontend\src\views\patient\dashboard.vue -->
<template>
  <div class="dashboard">
    <el-card class="stats-card">
      <h3 class="card-title">欢迎使用医疗预约系统</h3>
      <p class="card-desc">您可以在系统中进行预约挂号、查看预约记录等操作。</p>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-item">
          <div class="stat-icon appointments">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ appointmentCount }}</div>
            <div class="stat-label">我的预约</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-item">
          <div class="stat-icon pending">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ pendingCount }}</div>
            <div class="stat-label">待确认</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-item">
          <div class="stat-icon confirmed">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ confirmedCount }}</div>
            <div class="stat-label">已确认</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-item">
          <div class="stat-icon completed">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ completedCount }}</div>
            <div class="stat-label">已完成</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="recent-appointments" style="margin-top: 20px;">
      <template #header>
        <span>最近预约</span>
        <el-button type="text" @click="$router.push('/patient/appointments')">查看全部</el-button>
      </template>
      <el-table :data="recentAppointments" border>
        <el-table-column prop="doctorName" label="医生" />
        <el-table-column prop="departmentName" label="科室" />
        <el-table-column prop="appointmentDate" label="预约时间" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Calendar, Clock, CircleCheck } from '@element-plus/icons-vue'
import { appointmentApi } from '@/api'

const appointmentCount = ref(0)
const pendingCount = ref(0)
const confirmedCount = ref(0)
const completedCount = ref(0)
const recentAppointments = ref([])

const getStatusType = (status) => {
  const types = {
    'PENDING': 'warning',
    'CONFIRMED': 'primary',
    'COMPLETED': 'success',
    'CANCELLED': 'danger'
  }
  return types[status] || 'info'
}

const getStatusLabel = (status) => {
  const labels = {
    'PENDING': '待确认',
    'CONFIRMED': '已确认',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return labels[status] || status
}

const loadData = async () => {
  const patientId = localStorage.getItem('relatedId')
  if (!patientId) return

  try {
    const res = await appointmentApi.getByPatient(patientId)
    const appointments = res.data.data

    appointmentCount.value = appointments.length
    pendingCount.value = appointments.filter(a => a.status === 'PENDING').length
    confirmedCount.value = appointments.filter(a => a.status === 'CONFIRMED').length
    completedCount.value = appointments.filter(a => a.status === 'COMPLETED').length

    recentAppointments.value = appointments.slice(0, 5).map(a => ({
      doctorName: a.doctor?.name,
      departmentName: a.doctor?.department?.name,
      appointmentDate: a.appointmentDate,
      status: a.status
    }))
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard {
  padding: 10px;
}

.stats-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  margin-bottom: 20px;
}

.card-title {
  font-size: 20px;
  margin-bottom: 10px;
}

.card-desc {
  opacity: 0.9;
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 16px;
}

.stat-icon.appointments {
  background: #e8f4fd;
  color: #409eff;
}

.stat-icon.pending {
  background: #fef6ec;
  color: #e6a23c;
}

.stat-icon.confirmed {
  background: #f0f9eb;
  color: #67c23a;
}

.stat-icon.completed {
  background: #f0f5ff;
  color: #667eea;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.recent-appointments :deep(.el-tag) {
  font-size: 12px;
}
</style>