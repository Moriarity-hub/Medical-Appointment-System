<!-- D:\maven_project\mas\frontend\src\views\doctor\appointments.vue -->
<template>
  <div class="appointments-page">
    <el-card>
      <template #header>
        <span>我的预约</span>
      </template>

      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="全部" style="width: 150px;">
            <el-option label="全部" value="" />
            <el-option label="待确认" value="PENDING" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="loadAppointments">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="appointments" border>
        <el-table-column prop="id" label="预约编号" />
        <el-table-column prop="patientName" label="患者姓名" />
        <el-table-column prop="patientPhone" label="联系电话" />
        <el-table-column prop="appointmentDate" label="预约时间" />
        <el-table-column prop="symptoms" label="症状描述" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button v-if="scope.row.status === 'PENDING'" size="small" type="primary" @click="confirmAppointment(scope.row.id)">确认预约</el-button>
            <el-button v-if="scope.row.status === 'CONFIRMED'" size="small" type="success" @click="completeAppointment(scope.row.id)">完成就诊</el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { appointmentApi } from '@/api'

const appointments = ref([])
const filterForm = reactive({
  status: ''
})

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
    let data = res.data.data

    if (filterForm.status) {
      data = data.filter(a => a.status === filterForm.status)
    }

    appointments.value = data.map(a => ({
      id: a.id,
      patientName: a.patient?.name,
      patientPhone: a.patient?.phone,
      appointmentDate: a.appointmentDate,
      symptoms: a.symptoms,
      status: a.status
    }))
  } catch (error) {
    ElMessage.error('加载预约列表失败')
    console.error(error)
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
.appointments-page {
  padding: 10px;
}

.filter-form {
  margin-bottom: 20px;
}
</style>