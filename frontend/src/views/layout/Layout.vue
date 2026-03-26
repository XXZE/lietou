<template>
  <div class="app-wrapper">
    <div class="sidebar-container">
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :background-color="variables.menuBg"
          :text-color="variables.menuText"
          :unique-opened="false"
          :active-text-color="variables.menuActiveText"
          mode="vertical"
          router
        >
          <el-menu-item index="/dashboard">
            <i class="el-icon-s-home"></i>
            <span slot="title">工作台</span>
          </el-menu-item>
          <el-submenu index="/system">
            <template slot="title">
              <i class="el-icon-s-tools"></i>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/system/user">用户管理</el-menu-item>
            <el-menu-item index="/system/role">角色管理</el-menu-item>
            <el-menu-item index="/system/menu">菜单管理</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-scrollbar>
    </div>

    <div class="main-container">
      <div class="app-header">
        <div class="header-left">
          <i class="el-icon-s-unfold hamburger" @click="toggleSidebar"></i>
        </div>
        <div class="header-right">
          <el-dropdown class="user-dropdown" @command="handleCommand">
            <span class="el-dropdown-link">
              <i class="el-icon-user-solid"></i>
              <span class="user-name">{{ username }}</span>
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <tags-view />

      <div class="app-main">
        <transition name="fade-transform" mode="out-in">
          <router-view :key="key" />
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import TagsView from './components/TagsView'

export default {
  name: 'Layout',
  components: {
    TagsView
  },
  data() {
    return {
      isCollapse: false,
      variables: {
        menuBg: '#304156',
        menuText: '#bfcbd9',
        menuActiveText: '#409EFF'
      }
    }
  },
  computed: {
    username() {
      return this.$store.state.userInfo.nickname || this.$store.state.userInfo.username || '管理员'
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    key() {
      return this.$route.path
    }
  },
  methods: {
    toggleSidebar() {
      this.isCollapse = !this.isCollapse
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('logout')
          this.$message.success('退出成功')
          this.$router.push('/login')
        })
      } else if (command === 'profile') {
        this.$message.info('个人中心功能开发中')
      }
    }
  }
}
</script>
