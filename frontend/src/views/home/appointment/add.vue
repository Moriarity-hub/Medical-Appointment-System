<!-- D:\maven_project\mas\frontend\src\views\home\appointment\add.vue -->
<template>
  <div class="add-appointment">
    <el-card title="添加预约" class="appointment-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" class="appointment-form">
        <el-form-item label="患者" prop="patientId">
          <el-select v-model="form.patientId" placeholder="请选择患者" filterable>
            <el-option
                v-for="patient in patients"
                :key="patient.id"
                :label="patient.name + ' (' + patient.phone + ')'"
                :value="patient.id"
            />
          </el-select>
        </el-form-item>
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
        <el-form-item>
          <el-button type="primary" @click="submit" :loading="loading">保存</el-button>
          <el-button @click="$router.push('/home/appointments')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { appointmentApi, patientApi, doctorApi, departmentApi } from '@/api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const patients = ref([])
const departments = ref([])
const doctors = ref([])

const form = reactive({
  patientId: '',
  departmentId: '',
  doctorId: '',
  appointmentTime: ''
})

const rules = {
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
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
    const [patientRes, deptRes, doctorRes] = await Promise.all([
      patientApi.getAll(),
      departmentApi.getAll(),
      doctorApi.getAll()
    ])
    patients.value = patientRes.data.data
    departments.value = deptRes.data.data
    doctors.value = doctorRes.data.data.map(d => ({
      id: d.id,
      name: d.name,
      title: d.title,
      departmentId: d.department?.id
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
    await appointmentApi.create(form)
    ElMessage.success('创建成功')
    router.push('/home/appointments')
  } catch (error) {
    ElMessage.error('操作失败: ' + error.response?.data?.message)
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
  padding: 20px;
}

.appointment-card {
  max-width: 600px;
}

.appointment-form {
  margin-top: 20px;
}
</style>