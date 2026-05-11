<!-- D:\maven_project\mas\frontend\src\views\home\department\index.vue -->
<template>
  <div class="department-list">
    <div class="search-bar">
      <el-input
          v-model="searchText"
          placeholder="搜索科室名称"
          class="search-input"
          @keyup.enter="search"
      />
      <el-button type="primary" @click="search">搜索</el-button>
      <el-button type="success" @click="$router.push('/home/departments/add')">添加科室</el-button>
    </div>

    <el-table :data="departments" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="科室名称" />
      <el-table-column prop="description" label="科室描述" />
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="edit(scope.row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteDepartment(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { departmentApi } from '@/api'

const router = useRouter()
const departments = ref([])
const searchText = ref('')

const filteredDepartments = computed(() => {
  let data = departments.value
  if (searchText.value) {
    data = data.filter(d => d.name.includes(searchText.value))
  }
  return data
})

const loadDepartments = async () => {
  try {
    const res = await departmentApi.getAll()
    departments.value = res.data.data
  } catch (error) {
    console.error('加载科室列表失败:', error)
    ElMessage.error('加载科室列表失败')
  }
}

const search = () => {}

const edit = (id) => {
  router.push(`/home/departments/edit/${id}`)
}

const deleteDepartment = async (id) => {
  if (!confirm('确定要删除该科室吗？')) return
  try {
    await departmentApi.delete(id)
    ElMessage.success('删除成功')
    loadDepartments()
  } catch (error) {
    ElMessage.error('删除失败: ' + error.response?.data?.message)
  }
}

onMounted(() => {
  loadDepartments()
})
</script>

<style scoped>
.department-list {
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