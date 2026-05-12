<!-- D:\maven_project\mas\frontend\src\views\home\index.vue -->
<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background-color: #2f4050;">
      <div class="logo">
        <el-icon class="logo-icon"><OfficeBuilding /></el-icon>
        <span class="logo-text">医疗预约系统</span>
      </div>
      <el-menu
          :default-active="activeMenu"
          mode="vertical"
          theme="dark"
          class="sidebar-menu"
          @select="handleMenuSelect"
      >
        <el-menu-item index="/home">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-sub-menu index="patient">
          <template #title>
            <el-icon><User /></el-icon>
            <span>患者管理</span>
          </template>
          <el-menu-item index="/home/patients">患者列表</el-menu-item>
          <el-menu-item index="/home/patients/add">添加患者</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="doctor">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>医生管理</span>
          </template>
          <el-menu-item index="/home/doctors">医生列表</el-menu-item>
          <el-menu-item index="/home/doctors/add">添加医生</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="department">
          <template #title>
            <el-icon><House /></el-icon>
            <span>科室管理</span>
          </template>
          <el-menu-item index="/home/departments">科室列表</el-menu-item>
          <el-menu-item index="/home/departments/add">添加科室</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="appointment">
          <template #title>
            <el-icon><Calendar /></el-icon>
            <span>预约管理</span>
          </template>
          <el-menu-item index="/home/appointments">预约列表</el-menu-item>
          <el-menu-item index="/home/appointments/add">添加预约</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background-color: #fff; border-bottom: 1px solid #eee; padding: 0 20px;">
        <div style="display: flex; justify-content: space-between; align-items: center; height: 100%;">
          <h3 style="margin: 0; font-size: 18px; color: #333;">{{ pageTitle }}</h3>
          <div class="user-info" v-if="isLoggedIn">
            <el-dropdown>
              <span class="user-name">
                <el-icon><User /></el-icon>
                {{ userName }}
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="logout">
                    <el-icon><LogOut /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main style="padding: 20px; overflow-y: auto;">
        <router-view @update:title="updateTitle" />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {HomeFilled, User, UserFilled, OfficeBuilding, Calendar, ArrowDown, Link, House} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const pageTitle = ref('首页')
const userName = ref('')

const isLoggedIn = computed(() => {
  return localStorage.getItem('token') !== null
})

const activeMenu = computed(() => {
  return route.path
})

const titleMap = {
  '/home': '首页',
  '/home/patients': '患者列表',
  '/home/patients/add': '添加患者',
  '/home/doctors': '医生列表',
  '/home/doctors/add': '添加医生',
  '/home/departments': '科室列表',
  '/home/departments/add': '添加科室',
  '/home/appointments': '预约列表',
  '/home/appointments/add': '添加预约'
}

const updateTitle = (title) => {
  pageTitle.value = title
}

const updatePageTitle = () => {
  const fullPath = route.path
  const basePath = fullPath.startsWith('/home/patients/edit') ? '/home/patients/add' :
      fullPath.startsWith('/home/doctors/edit') ? '/home/doctors/add' :
          fullPath.startsWith('/home/departments/edit') ? '/home/departments/add' :
              fullPath
  pageTitle.value = titleMap[basePath] || '首页'
}

const handleMenuSelect = (key) => {
  router.push(key)
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('userName')
  localStorage.removeItem('phone')
  ElMessage.info('已退出登录')
  router.push('/login')
}

const checkLogin = () => {
  if (route.path !== '/login' && !isLoggedIn.value) {
    router.push('/login')
  }
}

watch(() => route.path, () => {
  updatePageTitle()
  checkLogin()
})

onMounted(() => {
  userName.value = localStorage.getItem('userName') || ''
  updatePageTitle()
  checkLogin()
})
</script>

<style scoped>
.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  border-bottom: 1px solid #3a4d63;
}

.logo-icon {
  font-size: 28px;
  color: #54a0ff;
  margin-right: 8px;
}

.logo-text {
  color: white;
  font-size: 16px;
  font-weight: 600;
}

.sidebar-menu {
  border-right: none;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-name {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  cursor: pointer;
  color: #666;
}

.user-name:hover {
  color: #333;
}
</style>