<!-- D:\maven_project\mas\frontend\src\views\patient\index.vue -->
<template>
  <div class="patient-layout">
    <el-container>
      <el-aside width="200px" class="aside">
        <div class="logo">
          <el-icon class="logo-icon"><OfficeBuilding /></el-icon>
          <span>医疗预约系统</span>
        </div>
        <el-menu :default-active="activeMenu" class="menu" router>
          <el-menu-item index="/patient">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/patient/appointments">
            <el-icon><Calendar /></el-icon>
            <span>我的预约</span>
          </el-menu-item>
          <el-menu-item index="/patient/appointments/add">
            <el-icon><Plus /></el-icon>
            <span>预约挂号</span>
          </el-menu-item>
          <el-menu-item index="/patient/profile">
            <el-icon><User /></el-icon>
            <span>个人资料</span>
          </el-menu-item>
        </el-menu>
        <div class="logout">
          <el-button @click="logout" type="text">退出登录</el-button>
        </div>
      </el-aside>
      <el-container>
        <el-header class="header">
          <span>欢迎, {{ userName }}</span>
        </el-header>
        <el-main class="main">
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
import {OfficeBuilding, Calendar, Plus, User, HomeFilled} from '@element-plus/icons-vue'

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
.patient-layout {
  min-height: 100vh;
}

.aside {
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  color: white;
  position: relative;
  min-height: 100vh;
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
}

.menu :deep(.el-menu-item:hover) {
  background: rgba(255,255,255,0.1);
}

.menu :deep(.el-menu-item.is-active) {
  background: rgba(255,255,255,0.2);
  color: white;
}

.logout {
  position: fixed;
  bottom: 20px;
  width: 200px;
  text-align: center;
}

.logout :deep(.el-button) {
  color: rgba(255,255,255,0.8);
}

.header {
  background: #fff;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 20px;
}

.main {
  padding: 20px;
}
</style>