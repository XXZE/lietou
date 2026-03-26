import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'

Vue.use(Router)

import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: 'Dashboard', icon: 'dashboard' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    name: 'System',
    meta: { title: '系统管理', icon: 'setting' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index'),
        meta: { title: '用户管理', icon: 'user' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role/index'),
        meta: { title: '角色管理', icon: 'peoples' }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index'),
        meta: { title: '菜单管理', icon: 'menu' }
      }
    ]
  }
]

const router = new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

router.beforeEach((to, from, next) => {
  const hasToken = store.getters.token
  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        next()
      } else {
        store.dispatch('user/getInfo').then(() => {
          next({ ...to, replace: true })
        })
      }
    }
  } else {
    if (to.path === '/login') {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

export default router
