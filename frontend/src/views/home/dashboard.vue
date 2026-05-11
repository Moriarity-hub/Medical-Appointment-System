<!-- D:\maven_project\mas\frontend\src\views\home\dashboard.vue -->
<template>
  <div class="dashboard">
    <el-card class="welcome-card">
      <div class="welcome-content">
        <h2>欢迎使用医疗预约系统</h2>
        <p>这是一个现代化的医疗预约管理系统，提供患者管理、医生管理、科室管理和预约管理等功能。</p>
      </div>
    </el-card>

    <div class="stats-grid">
      <el-card class="stat-card patient-card">
        <div class="stat-icon">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ patientCount }}</div>
          <div class="stat-label">患者总数</div>
        </div>
      </el-card>
      <el-card class="stat-card doctor-card">
        <div class="stat-icon">
          <el-icon><UserFilled /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ doctorCount }}</div>
          <div class="stat-label">医生总数</div>
        </div>
      </el-card>
      <el-card class="stat-card dept-card">
        <div class="stat-icon">
          <el-icon><Building /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ deptCount }}</div>
          <div class="stat-label">科室总数</div>
        </div>
      </el-card>
      <el-card class="stat-card appt-card">
        <div class="stat-icon">
          <el-icon><Calendar /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ apptCount }}</div>
          <div class="stat-label">预约总数</div>
        </div>
      </el-card>
    </div>

    <div class="recent-section">
      <el-card title="最近预约" class="recent-card">
        <el-table :data="recentAppointments" border>
          <el-table-column prop="id" label="预约ID" width="100" />
          <el-table-column prop="patientName" label="患者姓名" />
          <el-table-column prop="doctorName" label="医生姓名" />
          <el-table-column prop="departmentName" label="科室" />
          <el-table-column prop="appointmentTime" label="预约时间" width="180" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { patientApi, doctorApi, departmentApi, appointmentApi } from '@/api'

const patientCount = ref(0)
const doctorCount = ref(0)
const deptCount = ref(0)
const apptCount = ref(0)
const recentAppointments = ref([])

const loadStats = async () => {
  try {
    const [patientRes, doctorRes, deptRes, apptRes] = await Promise.all([
      patientApi.getAll(),
      doctorApi.getAll(),
      departmentApi.getAll(),
      appointmentApi.getAll()
    ])
    patientCount.value = patientRes.data.data.length
    doctorCount.value = doctorRes.data.data.length
    deptCount.value = deptRes.data.data.length
    apptCount.value = apptRes.data.data.length

    // 获取最近预约
    const appts = apptRes.data.data
    appts.sort((a, b) => new Date(b.appointmentTime) - new Date(a.appointmentTime))
    recentAppointments.value = appts.slice(0, 5).map(item => ({
      id: item.id,
      patientName: item.patient?.name || '-',
      doctorName: item.doctor?.name || '-',
      departmentName: item.doctor?.department?.name || '-',
      appointmentTime: item.appointmentTime,
      status: item.status
    }))
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const getStatusType = (status) => {
  const types = {
    'PENDING': 'warning',
    'CONFIRMED': 'success',
    'COMPLETED': 'info',
    'CANCELLED': 'danger'
  }
  return types[status] || 'default'
}

const getStatusText = (status) => {
  const texts = {
    'PENDING': '待确认',
    'CONFIRMED': '已确认',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return texts[status] || status
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.welcome-content h2 {
  margin: 0 0 12px 0;
  color: #333;
  font-size: 20px;
}

.welcome-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 24px;
  border-radius: 8px;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  margin-right: 20px;
}

.patient-card .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.doctor-card .stat-icon {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: white;
}

.dept-card .stat-icon {
  background: linear-gradient(135deg, #fc4a1a 0%, #f7b733 100%);
  color: white;
}

.appt-card .stat-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.recent-section {
  margin-top: 20px;
}

.recent-card {
  min-height: 300px;
}
</style>