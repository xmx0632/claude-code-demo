import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'
import router from '@/router'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  const isLoggedIn = computed(() => !!token.value)

  // 登录
  async function login(loginForm) {
    try {
      const res = await authApi.login(loginForm)
      token.value = res.data.token
      userInfo.value = res.data.user
      localStorage.setItem('token', res.data.token)
      ElMessage.success('登录成功')
      router.push({ name: 'TodoList' })
    } catch (error) {
      ElMessage.error(error.message || '登录失败')
      throw error
    }
  }

  // 注册
  async function register(registerForm) {
    try {
      const res = await authApi.register(registerForm)
      ElMessage.success('注册成功，请登录')
      router.push({ name: 'Login' })
    } catch (error) {
      ElMessage.error(error.message || '注册失败')
      throw error
    }
  }

  // 获取用户信息
  async function getUserInfo() {
    try {
      const res = await authApi.getInfo()
      userInfo.value = res.data
    } catch (error) {
      logout()
    }
  }

  // 退出登录
  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    router.push({ name: 'Login' })
    ElMessage.success('已退出登录')
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    register,
    getUserInfo,
    logout
  }
})
