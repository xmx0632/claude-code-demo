import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/components/Layout.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'DataAnalysis' }
      },
      {
        path: 'books',
        name: 'BookList',
        component: () => import('@/views/BookList.vue'),
        meta: { title: '图书管理', icon: 'Books' }
      },
      {
        path: 'books/create',
        name: 'BookCreate',
        component: () => import('@/views/BookForm.vue'),
        meta: { title: '新增图书', hidden: true }
      },
      {
        path: 'books/:id/edit',
        name: 'BookEdit',
        component: () => import('@/views/BookForm.vue'),
        meta: { title: '编辑图书', hidden: true }
      },
      {
        path: 'categories',
        name: 'CategoryList',
        component: () => import('@/views/CategoryList.vue'),
        meta: { title: '分类管理', icon: 'Menu' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
