<template>
  <div class="app-wrapper">
    <div class="sidebar-container">
      <logo :collapse="isCollapse" />
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :background-color="variables.menuBg"
          :text-color="variables.menuText"
          :unique-opened="false"
          :active-text-color="variables.menuActiveText"
          mode="vertical"
        >
          <sidebar-item v-for="route in sidebarRouters" :key="route.path" :item="route" :base-path="route.path" />
        </el-menu>
      </el-scrollbar>
    </div>

    <div class="main-container">
      <div class="navbar">
        <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
        <breadcrumb class="breadcrumb-container" />

        <div class="right-menu">
          <el-dropdown class="avatar-container" trigger="click">
            <div class="avatar-wrapper">
              <i class="el-icon-user"></i>
              <span class="user-name">{{ name }}</span>
              <i class="el-icon-caret-bottom"></i>
            </div>
            <el-dropdown-menu slot="dropdown" class="user-dropdown">
              <el-dropdown-item @click.native="logout">
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <app-main />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './components/Sidebar/Logo'
import SidebarItem from './components/Sidebar/SidebarItem'
import AppMain from './components/AppMain'
import Breadcrumb from './components/Breadcrumb'
import Hamburger from './components/Hamburger'

export default {
  components: {
    SidebarItem,
    Logo,
    AppMain,
    Breadcrumb,
    Hamburger
  },
  data() {
    return {
      variables: {
        menuBg: '#304156',
        menuText: '#bfcbd9',
        menuActiveText: '#409EFF'
      }
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'sidebarRouters'
    ]),
    sidebarRouters() {
      return this.$store.state.permission.sidebarRouters || []
    },
    sidebar() {
      return {
        opened: !this.$store.state.app.sidebar.opened
      }
    },
    isCollapse() {
      return !this.$store.state.app.sidebar.opened
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('user/logout').then(() => {
          location.href = '/login'
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  display: flex;
  width: 100%;
  height: 100%;

  .sidebar-container {
    width: 210px;
    background-color: #304156;
    height: 100%;
    position: fixed;
    font-size: 0;
    top: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;

    .el-scrollbar {
      height: 100%;
    }
  }

  .main-container {
    flex: 1;
    margin-left: 210px;
    position: relative;

    .navbar {
      height: 50px;
      overflow: hidden;
      position: relative;
      background: #fff;
      box-shadow: 0 1px 4px rgba(0,21,41,.08);
      display: flex;
      align-items: center;

      .hamburger-container {
        line-height: 46px;
        height: 100%;
        float: left;
        cursor: pointer;
        transition: background .3s;
        -webkit-tap-highlight-color:transparent;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }

      .breadcrumb-container {
        float: left;
      }

      .right-menu {
        margin-left: auto;
        margin-right: 20px;
        display: flex;
        align-items: center;

        .avatar-container {
          cursor: pointer;

          .avatar-wrapper {
            display: flex;
            align-items: center;

            i {
              font-size: 18px;
              margin-right: 5px;
            }

            .user-name {
              margin-right: 5px;
            }
          }
        }
      }
    }
  }
}
</style>
