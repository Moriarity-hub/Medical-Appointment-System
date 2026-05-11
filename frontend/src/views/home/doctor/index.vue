<!-- D:\maven_project\mas\frontend\src\views\home\doctor\index.vue -->
<template>
  <div class="doctor-list">
    <div class="search-bar">
      <el-input
          v-model="searchText"
          placeholder="搜索医生姓名"
          class="search-input"
          @keyup.enter="search"
      />
      <el-button type="primary" @click="search">搜索</el-button>
      <el-button type="success" @click="$router.push('/home/doctors/add')">添加医生</el-button>
    </div>

    <el-table :data="doctors" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="title" label="职称" />
      <el-table-column prop="specialty" label="专业领域" />
      <el-table-column prop="phone" label="联系电话" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="department.name" label="所属科室" />
      <el-table-column prop="available" label="是否可用" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.available ? 'success' : 'danger'">
            {{ scope.row.available ? '可用' : '不可用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="edit(scope.row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteDoctor(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { doctorApi } from '@/api'

const router = useRouter()
const doctors = ref([])
const searchText = ref('')

const filteredDoctors = computed(() => {
  let data = doctors.value
  if (searchText.value) {
    data = data.filter(d => d.name.includes(searchText.value))
  }
  return data
})

const loadDoctors = async () => {
  try {
    const res = await doctorApi.getAll()
    doctors.value = res.data.data
  } catch (error) {
    console.error('加载医生列表失败:', error)
    ElMessage.error('加载医生列表失败')
  }
}

const search = () => {}

const edit = (id) => {
  router.push(`/home/doctors/edit/${id}`)
}

const deleteDoctor = async (id) => {
  if (!confirm('确定要删除该医生吗？')) return
  try {
    await doctorApi.delete(id)
    ElMessage.success('删除成功')
    loadDoctors()
  } catch (error) {
    ElMessage.error('删除失败: ' + error.response?.data?.message)
  }
}

onMounted(() => {
  loadDoctors()
})
</script>

<style scoped>
.doctor-list {
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
</style>