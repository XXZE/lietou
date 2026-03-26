import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index')
  },
  {
    path: '/',
    name: 'Layout',
    redirect: '/dashboard',
    component: () => import('@/views/layout/Layout'),
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: '工作台', icon: 'dashboard' }
      },
      {
        path: 'system/user',
        name: 'User',
        component: () => import('@/views/system/user/index'),
        meta: { title: '用户管理', icon: 'user' }
      },
      {
        path: 'system/role',
        name: 'Role',
        component: () => import('@/views/system/role/index'),
        meta: { title: '角色管理', icon: 'peoples' }
      },
      {
        path: 'system/menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index'),
        meta: { title: '菜单管理', icon: 'menu' }
      },
      {
        path: 'crm/client',
        name: 'Client',
        component: () => import('@/views/crm/client/index'),
        meta: { title: '客户管理', icon: 'office-building' }
      },
      {
        path: 'crm/job',
        name: 'Job',
        component: () => import('@/views/crm/job/index'),
        meta: { title: '职位管理', icon: 'briefcase' }
      },
      {
        path: 'talent/candidate',
        name: 'Candidate',
        component: () => import('@/views/talent/candidate/index'),
        meta: { title: '候选人管理', icon: 'user' }
      },
      {
        path: 'talent/tag',
        name: 'Tag',
        component: () => import('@/views/talent/tag/index'),
        meta: { title: '标签管理', icon: 'price-tag' }
      },
      {
        path: 'process/recommendation',
        name: 'Recommendation',
        component: () => import('@/views/process/recommendation/index'),
        meta: { title: '推荐管理', icon: 'document' }
      },
      {
        path: 'report/performance',
        name: 'Performance',
        component: () => import('@/views/report/performance/index'),
        meta: { title: '业绩报表', icon: 'data-line' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
