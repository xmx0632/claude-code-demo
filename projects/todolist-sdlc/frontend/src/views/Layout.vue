<template>
  <div class="layout-container">
    <header class="layout-header">
      <div class="logo">📋 TodoList</div>
      <div class="user-info">
        <span class="username">{{ userStore.userInfo?.nickname || '用户' }}</span>
        <el-dropdown @command="handleCommand">
          <el-avatar :size="32" class="avatar">
            {{ userStore.userInfo?.nickname?.charAt(0) || 'U' }}
          </el-avatar>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <main class="layout-main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

onMounted(() => {
  if (!userStore.userInfo) {
    userStore.getUserInfo()
  }
})

function handleCommand(command) {
  if (command === 'logout') {
    userStore.logout()
  }
}
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.layout-header {
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.logo {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  color: #666;
}

.avatar {
  cursor: pointer;
  background: #409eff;
  color: #fff;
}

.layout-main {
  padding: 80px 24px 24px;
  max-width: 1200px;
  margin: 0 auto;
}
</style>
