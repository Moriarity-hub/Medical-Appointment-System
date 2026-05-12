<!-- D:\maven_project\mas\frontend\src\views\home\patient\index.vue -->
<template>
  <div class="patient-list">
    <div class="search-bar">
      <el-input
          v-model="searchText"
          placeholder="搜索患者姓名或电话"
          class="search-input"
          @keyup.enter="search"
      />
      <el-button type="primary" @click="search">搜索</el-button>
      <el-button type="success" @click="$router.push('/home/patients/add')">添加患者</el-button>
    </div>

    <el-table :data="filteredPatients" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="idCard" label="身份证号" />
      <el-table-column prop="phone" label="联系电话" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="edit(scope.row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deletePatient(scope.row.id)">删除</el-button>
          <el-button size="small" @click="viewAppointments(scope.row.id)">查看预约</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @current-change="handlePageChange"
        layout="total, prev, pager, next"
        class="pagination"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { patientApi } from '@/api'

const router = useRouter()
const patients = ref([])
const searchText = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const allPatients = ref([])

const filteredPatients = computed(() => {
  let data = allPatients.value
  if (searchText.value) {
    data = data.filter(p =>
        p.name.includes(searchText.value) || p.phone.includes(searchText.value)
    )
  }
  total.value = data.length
  const start = (currentPage.value - 1) * pageSize.value
  return data.slice(start, start + pageSize.value)
})

const loadPatients = async () => {
  try {
    const res = await patientApi.getAll()
    allPatients.value = res.data.data
  } catch (error) {
    console.error('加载患者列表失败:', error)
    ElMessage.error('加载患者列表失败')
  }
}

const search = () => {
  currentPage.value = 1
}

const edit = (id) => {
  router.push(`/home/patients/edit/${id}`)
}

const deletePatient = async (id) => {
  if (!confirm('确定要删除该患者吗？')) return
  try {
    await patientApi.delete(id)
    ElMessage.success('删除成功')
    loadPatients()
  } catch (error) {
    ElMessage.error('删除失败: ' + error.response?.data?.message)
  }
}

const viewAppointments = (id) => {
  router.push('/home/appointments')
}

const handlePageChange = (page) => {
  currentPage.value = page
}

onMounted(() => {
  loadPatients()
})
</script>

<style scoped>
.patient-list {
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

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>