<!-- D:\maven_project\mas\frontend\src\views\login\index.vue -->
<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-header">
        <el-icon class="login-icon"><Hospital /></el-icon>
        <h2 class="login-title">医疗预约系统</h2>
      </div>

      <!-- 切换登录/注册 -->
      <div class="tab-switch">
        <span
            :class="{ active: isLogin }"
            @click="isLogin = true"
        >登录</span>
        <span
            :class="{ active: !isLogin }"
            @click="isLogin = false"
        >注册</span>
      </div>

      <!-- 登录表单 -->
      <el-form v-if="isLogin" :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px" class="form"  @submit.prevent>
        <el-form-item label="手机号" prop="phone">
          <el-input
              v-model="loginForm.phone"
              placeholder="请输入手机号"
              type="tel"
              prefix-icon="Phone"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
              v-model="loginForm.password"
              placeholder="请输入密码"
              type="password"
              prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login" class="submit-btn" :loading="loading">登录</el-button>
        </el-form-item>
        <div class="form-tips">
          <p>提示：初始密码为手机号后6位</p>
        </div>
      </el-form>

      <!-- 注册表单 -->
      <el-form v-else :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="100px" class="form">
        <el-form-item label="患者姓名" prop="name">
          <el-input
              v-model="registerForm.name"
              placeholder="请输入姓名"
              prefix-icon="User"
          />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input
              v-model="registerForm.idCard"
              placeholder="请输入身份证号（选填）"
              prefix-icon="CreditCard"
          />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input
              v-model="registerForm.phone"
              placeholder="请输入手机号"
              type="tel"
              prefix-icon="Phone"
          />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input
              v-model="registerForm.email"
              placeholder="请输入邮箱（选填）"
              type="email"
              prefix-icon="Mail"
          />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input
              v-model="registerForm.address"
              placeholder="请输入地址（选填）"
              prefix-icon="MapLocation"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
              v-model="registerForm.password"
              placeholder="请设置密码（不设置则默认为手机号后6位）"
              type="password"
              prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register" class="submit-btn" :loading="loading">注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api'

const router = useRouter()
const isLogin = ref(true)
const loading = ref(false)

// 登录表单
const loginFormRef = ref(null)
const loginForm = reactive({
  phone: '',
  password: ''
})

const loginRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ]
}

// 注册表单
const registerFormRef = ref(null)
const registerForm = reactive({
  name: '',
  idCard: '',
  phone: '',
  email: '',
  address: '',
  password: ''
})

const registerRules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  idCard: [
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
}

const login = async () => {
  if (!loginFormRef.value) return
  const valid = await loginFormRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    const res = await authApi.login(loginForm)
    if (res.data.code === 200) {
      localStorage.setItem('token', res.data.data.token)
      localStorage.setItem('userId', res.data.data.userId)
      localStorage.setItem('userName', res.data.data.userName)
      localStorage.setItem('phone', res.data.data.phone)
      ElMessage.success('登录成功')
      router.push('/home')
    } else {
      ElMessage.error(res.data.message || '登录失败')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '登录失败')
  } finally {
    loading.value = false
  }
}

const register = async () => {
  if (!registerFormRef.value) return
  const valid = await registerFormRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    const res = await authApi.register(registerForm)
    if (res.data.code === 200) {
      localStorage.setItem('token', res.data.data.token)
      localStorage.setItem('userId', res.data.data.userId)
      localStorage.setItem('userName', res.data.data.userName)
      localStorage.setItem('phone', res.data.data.phone)
      ElMessage.success('注册成功')
      router.push('/home')
    } else {
      ElMessage.error(res.data.message || '注册失败')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 450px;
  padding: 40px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
}

.login-header {
  text-align: center;
  margin-bottom: 20px;
}

.login-icon {
  font-size: 48px;
  color: #5470c6;
  margin-bottom: 12px;
}

.login-title {
  margin: 0;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.tab-switch {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.tab-switch span {
  cursor: pointer;
  font-size: 16px;
  color: #666;
  padding-bottom: 8px;
  border-bottom: 2px solid transparent;
  transition: all 0.3s;
}

.tab-switch span.active {
  color: #5470c6;
  border-bottom-color: #5470c6;
}

.form {
  margin-top: 10px;
}

.submit-btn {
  width: 100%;
  height: 42px;
  font-size: 16px;
}

.form-tips {
  text-align: center;
  margin-top: 16px;
  color: #999;
  font-size: 12px;
}
</style>