// src/stores/user.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
    const token = ref(localStorage.getItem('token') || '')
    const userId = ref(localStorage.getItem('userId') || '')
    const userName = ref(localStorage.getItem('userName') || '')
    const phone = ref(localStorage.getItem('phone') || '')

    const login = (data) => {
        token.value = data.token
        userId.value = data.userId
        userName.value = data.userName
        phone.value = data.phone
        localStorage.setItem('token', data.token)
        localStorage.setItem('userId', data.userId)
        localStorage.setItem('userName', data.userName)
        localStorage.setItem('phone', data.phone)
    }

    const logout = () => {
        token.value = ''
        userId.value = ''
        userName.value = ''
        phone.value = ''
        localStorage.removeItem('token')
        localStorage.removeItem('userId')
        localStorage.removeItem('userName')
        localStorage.removeItem('phone')
    }

    const isLoggedIn = () => !!token.value

    return { token, userId, userName, phone, login, logout, isLoggedIn }
})