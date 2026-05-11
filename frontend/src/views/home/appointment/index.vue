<!-- D:\maven_project\mas\frontend\src\views\home\appointment\index.vue -->
<template>
  <div class="appointment-list">
    <div class="search-bar">
      <el-input
          v-model="searchText"
          placeholder="搜索患者姓名或医生姓名"
          class="search-input"
          @keyup.enter="search"
      />
      <el-select v-model="statusFilter" placeholder="状态筛选" class="status-select">
        <el-option label="全部" value="" />
        <el-option label="待确认" value="PENDING" />
        <el-option label="已确认" value="CONFIRMED" />
        <el-option label="已完成" value="COMPLETED" />
        <el-option label="已取消" value="CANCELLED" />
      </el-select>
      <el-button type="primary" @click="search">搜索</el-button>
      <el-button type="success" @click="$router.push('/home/appointments/add')">添加预约</el-button>
    </div>

    <el-table :data="appointments" border stripe>
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
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button
              v-if="scope.row.status === 'PENDING'"
              size="small"
              type="success"
              @click="confirmAppointment(scope.row.id)"
          >确认预约</el-button>
          <el-button
              v-if="scope.row.status !== 'CANCELLED' && scope.row.status !== 'COMPLETED'"
              size="small"
              type="danger"
              @click="cancelAppointment(scope.row.id)"
          >取消预约</el-button>
          <el-button
              v-if="scope.row.status === 'CONFIRMED'"
              size="small"
              @click="completeAppointment(scope.row.id)"
          >完成就诊</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { appointmentApi } from '@/api'

const appointments = ref([])
const searchText = ref('')
const statusFilter = ref('')

const filteredAppointments = computed(() => {
  let data = appointments.value
  if (searchText.value) {
    data = data.filter(a =>
        a.patientName.includes(searchText.value) || a.doctorName.includes(searchText.value)
    )
  }
  if (statusFilter.value) {
    data = data.filter(a => a.status === statusFilter.value)
  }
  return data
})

const loadAppointments = async () => {
  try {
    const res = await appointmentApi.getAll()
    appointments.value = res.data.data.map(item => ({
      id: item.id,
      patientName: item.patient?.name || '-',
      doctorName: item.doctor?.name || '-',
      departmentName: item.department?.name || '-',
      appointmentTime: item.appointmentTime,
      status: item.status,
      createdAt: item.createdAt
    }))
  } catch (error) {
    console.error('加载预约列表失败:', error)
    ElMessage.error('加载预约列表失败')
  }
}

const search = () => {}

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

const confirmAppointment = async (id) => {
  try {
    await appointmentApi.updateStatus(id, 'CONFIRMED')
    ElMessage.success('预约已确认')
    loadAppointments()
  } catch (error) {
    ElMessage.error('操作失败: ' + error.response?.data?.message)
  }
}

const cancelAppointment = async (id) => {
  if (!confirm('确定要取消该预约吗？')) return
  try {
    await appointmentApi.cancel(id)
    ElMessage.success('预约已取消')
    loadAppointments()
  } catch (error) {
    ElMessage.error('操作失败: ' + error.response?.data?.message)
  }
}

const completeAppointment = async (id) => {
  try {
    await appointmentApi.updateStatus(id, 'COMPLETED')
    ElMessage.success('就诊已完成')
    loadAppointments()
  } catch (error) {
    ElMessage.error('操作失败: ' + error.response?.data?.message)
  }
}

onMounted(() => {
  loadAppointments()
})
</script>

<style scoped>
.appointment-list {
  padding: 20px;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.search-input {
  width: 350px;
}

.status-select {
  width: 150px;
}
</style>