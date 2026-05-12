<!-- D:\maven_project\mas\frontend\src\views\doctor\index.vue -->
<template>
  <div class="doctor-layout">
    <el-container>
      <el-aside width="200px" class="aside">
        <div class="logo">
          <el-icon class="logo-icon"><Monitor /></el-icon>
          <span>医疗预约系统</span>
        </div>
        <el-menu :default-active="activeMenu" class="menu" router>
          <el-menu-item index="/doctor">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/doctor/appointments">
            <el-icon><Calendar /></el-icon>
            <span>我的预约</span>
          </el-menu-item>
          <el-menu-item index="/doctor/profile">
            <el-icon><User /></el-icon>
            <span>个人资料</span>
          </el-menu-item>
        </el-menu>
        <div class="logout">
          <el-button @click="logout" type="text">退出登录</el-button>
        </div>
      </el-aside>
      <el-container class="main-container">
        <el-header class="header">
          <div class="welcome-text">
            <el-icon class="user-icon"><User /></el-icon>
            <span>欢迎, {{ userName }}</span>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Monitor, HomeFilled, Calendar, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userName = ref('')

const activeMenu = computed(() => route.path)

const logout = () => {
  localStorage.clear()
  ElMessage.success('退出成功')
  router.push('/login')
}

onMounted(() => {
  userName.value = localStorage.getItem('userName') || ''
})
</script>

<style scoped>
.doctor-layout {
  min-height: 100vh;
  display: flex;
}

.aside {
  background: linear-gradient(180deg, #20c997 0%, #17a2b8 100%);
  color: white;
  position: relative;
  min-height: 100vh;
  flex-shrink: 0;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  border-bottom: 1px solid rgba(255,255,255,0.2);
}

.logo-icon {
  font-size: 24px;
  margin-right: 8px;
}

.menu {
  border-right: none;
  background: transparent;
  color: white;
}

.menu :deep(.el-menu-item) {
  color: rgba(255,255,255,0.9);
  height: 48px;
  line-height: 48px;
}

.menu :deep(.el-menu-item:hover) {
  background: rgba(255,255,255,0.1);
}

.menu :deep(.el-menu-item.is-active) {
  background: rgba(255,255,255,0.2);
  color: white;
}

.logout {
  position: absolute;
  bottom: 20px;
  width: 100%;
  text-align: center;
}

.logout :deep(.el-button) {
  color: rgba(255,255,255,0.8);
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.header {
  background: #ffffff;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 24px;
  height: 60px;
}

.welcome-text {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  font-size: 14px;
}

.user-icon {
  font-size: 18px;
  color: #20c997;
}

.main-content {
  flex: 1;
  padding: 20px;
  background: #f5f5f5;
  overflow-y: auto;
}
</style>