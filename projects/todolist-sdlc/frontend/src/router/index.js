import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/views/Layout.vue'),
    redirect: '/todos',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'todos',
        name: 'TodoList',
        component: () => import('@/views/TodoList.vue'),
        meta: { title: '待办事项' }
      },
      {
        path: 'tags',
        name: 'TagManage',
        component: () => import('@/views/TagManage.vue'),
        meta: { title: '标签管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - TodoList` : 'TodoList'

  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next({ name: 'Login' })
  } else if ((to.name === 'Login' || to.name === 'Register') && userStore.isLoggedIn) {
    next({ name: 'TodoList' })
  } else {
    next()
  }
})

export default router
