<!-- D:\maven_project\mas\frontend\src\views\doctor\dashboard.vue -->
<template>
  <div class="dashboard">
    <el-card class="stats-card">
      <h3 class="card-title">欢迎使用医疗预约系统</h3>
      <p class="card-desc">您可以在系统中查看预约、确认预约和完成就诊。</p>
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
        <span>待处理预约</span>
        <el-button type="text" @click="$router.push('/doctor/appointments')">查看全部</el-button>
      </template>
      <el-table :data="pendingAppointments" border>
        <el-table-column prop="patientName" label="患者姓名" />
        <el-table-column prop="patientPhone" label="联系电话" />
        <el-table-column prop="appointmentDate" label="预约时间" />
        <el-table-column prop="symptoms" label="症状描述" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button v-if="scope.row.status === 'PENDING'" size="small" type="primary" @click="confirmAppointment(scope.row.id)">确认</el-button>
            <el-button v-if="scope.row.status === 'CONFIRMED'" size="small" type="success" @click="completeAppointment(scope.row.id)">完成就诊</el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Calendar, Clock, CircleCheck } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { appointmentApi } from '@/api'

const appointmentCount = ref(0)
const pendingCount = ref(0)
const confirmedCount = ref(0)
const completedCount = ref(0)
const pendingAppointments = ref([])

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

const loadAppointments = async () => {
  const doctorId = localStorage.getItem('relatedId')
  if (!doctorId) return

  try {
    const res = await appointmentApi.getByDoctor(doctorId)
    const data = res.data.data

    appointmentCount.value = data.length
    pendingCount.value = data.filter(a => a.status === 'PENDING').length
    confirmedCount.value = data.filter(a => a.status === 'CONFIRMED').length
    completedCount.value = data.filter(a => a.status === 'COMPLETED').length

    // 只显示待确认和已确认的预约
    pendingAppointments.value = data
      .filter(a => a.status === 'PENDING' || a.status === 'CONFIRMED')
      .slice(0, 10)
      .map(a => ({
        id: a.id,
        patientName: a.patient?.name,
        patientPhone: a.patient?.phone,
        appointmentDate: a.appointmentDate,
        symptoms: a.symptoms,
        status: a.status
      }))
  } catch (error) {
    console.error('加载预约列表失败:', error)
    ElMessage.error('加载预约列表失败')
  }
}

const confirmAppointment = async (id) => {
  try {
    await appointmentApi.updateStatus(id, 'CONFIRMED')
    ElMessage.success('预约已确认')
    loadAppointments()
  } catch (error) {
    ElMessage.error('确认失败')
    console.error(error)
  }
}

const completeAppointment = async (id) => {
  try {
    await appointmentApi.updateStatus(id, 'COMPLETED')
    ElMessage.success('就诊已完成')
    loadAppointments()
  } catch (error) {
    ElMessage.error('完成失败')
    console.error(error)
  }
}

onMounted(() => {
  loadAppointments()
})
</script>

<style scoped>
.dashboard {
  padding: 10px;
}

.stats-card {
  margin-bottom: 20px;
}

.card-title {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 20px;
}

.card-desc {
  margin: 0;
  color: #666;
}

.stat-item {
  text-align: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
  font-size: 28px;
}

.stat-icon.appointments {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.stat-icon.pending {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.stat-icon.confirmed {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.stat-icon.completed {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  color: #999;
  font-size: 14px;
}

.recent-appointments {
  margin-top: 20px;
}
</style>