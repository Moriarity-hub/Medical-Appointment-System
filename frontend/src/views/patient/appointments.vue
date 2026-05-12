<!-- D:\maven_project\mas\frontend\src\views\patient\appointments.vue -->
<template>
  <div class="appointments-page">
    <el-card>
      <template #header>
        <span>我的预约</span>
        <el-button type="primary" @click="$router.push('/patient/appointments/add')">
          <el-icon><Plus /></el-icon>
          预约挂号
        </el-button>
      </template>

      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="状态" prop="status" >
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
        <el-table-column prop="doctorName" label="医生" />
        <el-table-column prop="departmentName" label="科室" />
        <el-table-column prop="appointmentDate" label="预约时间" />
        <el-table-column prop="symptoms" label="症状描述" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button v-if="scope.row.status === 'PENDING'" type="text" @click="cancelAppointment(scope.row.id)">取消</el-button>
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
import { Plus } from '@element-plus/icons-vue'
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
  const patientId = localStorage.getItem('relatedId')
  if (!patientId) return

  try {
    const res = await appointmentApi.getByPatient(patientId)
    let data = res.data.data

    if (filterForm.status) {
      data = data.filter(a => a.status === filterForm.status)
    }

    appointments.value = data.map(a => ({
      id: a.id,
      doctorName: a.doctor?.name + ' (' + a.doctor?.title + ')',
      departmentName: a.doctor?.department?.name,
      appointmentDate: a.appointmentDate,
      symptoms: a.symptoms,
      status: a.status
    }))
  } catch (error) {
    ElMessage.error('加载预约列表失败')
    console.error(error)
  }
}

const cancelAppointment = async (id) => {
  try {
    await appointmentApi.cancel(id)
    ElMessage.success('预约已取消')
    await loadAppointments()
  } catch (error) {
    ElMessage.error('取消预约失败')
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