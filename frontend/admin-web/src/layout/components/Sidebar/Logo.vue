<template>
  <div class="logo-container">
    <transition name="sidebarLogoFade">
      <router-link key="expand" class="sidebar-logo-link" to="/">
        <h1 class="sidebar-title">{{ title }}</h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'Logo',
  props: {
    collapse: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      title: '管理平台'
    }
  }
}
</script>

<style lang="scss" scoped>
.logo-container {
  position: relative;
  height: 50px;
  line-height: 50px;
  text-align: center;
  overflow: hidden;

  .sidebar-logo-link {
    height: 100%;
    width: 100%;

    .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-size: 18px;
      font-weight: 600;
    }
  }
}
</style>
