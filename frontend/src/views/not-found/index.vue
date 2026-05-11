<!-- D:\maven_project\mas\frontend\src\views\not-found\index.vue -->
<template>
  <div class="not-found">
    <div class="not-found-content">
      <el-icon class="not-found-icon"><WarningFilled /></el-icon>
      <h2>404 - 页面未找到</h2>
      <p>您访问的页面不存在</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/home')
}
</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.not-found-content {
  text-align: center;
  padding: 40px;
}

.not-found-icon {
  font-size: 80px;
  color: #999;
  margin-bottom: 20px;
}

.not-found-content h2 {
  margin: 0 0 12px 0;
  color: #333;
  font-size: 24px;
}

.not-found-content p {
  margin: 0 0 20px 0;
  color: #666;
}
</style>