<!-- D:\maven_project\mas\frontend\src\views\patient\add-appointments.vue -->
<template>
  <div class="add-appointment">
    <el-card title="预约挂号" class="appointment-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" class="appointment-form">
        <el-form-item label="科室" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="请选择科室" @change="onDepartmentChange">
            <el-option
                v-for="dept in departments"
                :key="dept.id"
                :label="dept.name"
                :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="医生" prop="doctorId">
          <el-select v-model="form.doctorId" placeholder="请选择医生">
            <el-option
                v-for="doctor in filteredDoctors"
                :key="doctor.id"
                :label="doctor.name + ' (' + doctor.title + ')'"
                :value="doctor.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker
              v-model="form.appointmentTime"
              type="datetime"
              placeholder="请选择预约时间"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="症状描述" prop="symptoms">
          <el-input
              v-model="form.symptoms"
              type="textarea"
              placeholder="请描述您的症状（选填）"
              :rows="3"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit" :loading="loading">提交预约</el-button>
          <el-button @click="$router.push('/patient/appointments')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { appointmentApi, doctorApi, departmentApi } from '@/api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const departments = ref([])
const doctors = ref([])

const form = reactive({
  departmentId: '',
  doctorId: '',
  appointmentTime: '',
  symptoms: ''
})

const rules = {
  departmentId: [{ required: true, message: '请选择科室', trigger: 'change' }],
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  appointmentTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }]
}

const filteredDoctors = computed(() => {
  if (!form.departmentId) return []
  return doctors.value.filter(d => d.departmentId === form.departmentId && d.available)
})

const loadData = async () => {
  try {
    const [deptRes, doctorRes] = await Promise.all([
      departmentApi.getAll(),
      doctorApi.getAll()
    ])
    departments.value = deptRes.data.data
    doctors.value = doctorRes.data.data.map(d => ({
      id: d.id,
      name: d.name,
      title: d.title,
      departmentId: d.department?.id,
      available: d.available
    }))
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

const onDepartmentChange = () => {
  form.doctorId = ''
}

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

const submit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    const patientId = localStorage.getItem('relatedId')
    await appointmentApi.create({
      patientId: patientId,
      doctorId: form.doctorId,
      appointmentDate: form.appointmentTime,
      symptoms: form.symptoms
    })
    ElMessage.success('预约成功')
    router.push('/patient/appointments')
  } catch (error) {
    ElMessage.error('预约失败: ' + error.response?.data?.message)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.add-appointment {
  padding: 10px;
}

.appointment-card {
  max-width: 600px;
}

.appointment-form {
  margin-top: 20px;
}
</style>