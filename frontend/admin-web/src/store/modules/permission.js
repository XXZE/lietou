import { constantRoutes } from '@/router'

const state = {
  routes: [],
  addRoutes: [],
  sidebarRouters: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  },
  SET_SIDE_BAR_ROUTES: (state, routers) => {
    state.sidebarRouters = routers
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      let accessedRoutes
      if (roles.includes('SUPER_ADMIN')) {
        accessedRoutes = constantRoutes || []
      } else {
        accessedRoutes = filterAsyncRoutes(constantRoutes, roles)
      }
      commit('SET_ROUTES', accessedRoutes)
      commit('SET_SIDE_BAR_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

function filterAsyncRoutes(routes, roles) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })
  return res
}

function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
