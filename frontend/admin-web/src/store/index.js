import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import permission from './modules/permission'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
    permission
  },
  getters: {
    token: state => state.user.token,
    avatar: state => state.user.avatar,
    name: state => state.user.name,
    roles: state => state.user.roles,
    permissions: state => state.permission.permissions,
    sidebarRouters: state => state.permission.sidebarRouters
  }
})

export default store
