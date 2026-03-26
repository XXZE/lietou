<template>
  <div class="tags-view-container">
    <router-link
      v-for="tag in visitedViews"
      :key="tag.path"
      :to="{ path: tag.path, query: tag.query }"
      tag="span"
      class="tags-view-item"
      :class="isActive(tag) ? 'active' : ''"
      @click.middle.native="!isAffix(tag) ? closeSelectedTag(tag) : ''"
      @contextmenu.prevent.native="openMenu(tag, $event)"
    >
      {{ tag.meta.title }}
      <span v-if="!isAffix(tag)" class="el-icon-close" @click.prevent.stop="closeSelectedTag(tag)" />
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'TagsView',
  data() {
    return {
      visible: false,
      top: 0,
      left: 0,
      visitedViews: [],
      affixTags: []
    }
  },
  watch: {
    $route() {
      this.addTags()
    }
  },
  mounted() {
    this.addTags()
  },
  methods: {
    isActive(route) {
      return route.path === this.$route.path || route.path === this.$route.path.replace(/\/$/, '')
    },
    isAffix(tag) {
      return tag.meta && tag.meta.affix
    },
    addTags() {
      const { name } = this.$route
      if (name) {
        const view = { ...this.$route }
        const exist = this.visitedViews.some(v => v.path === view.path)
        if (!exist) {
          this.visitedViews.push(view)
        }
      }
      return false
    },
    closeSelectedTag(view) {
      this.visitedViews = this.visitedViews.filter(tag => tag.path !== view.path)
      if (this.isActive(view)) {
        this.toLastView(this.visitedViews, view)
      }
    },
    toLastView(visitedViews, view) {
      const latestView = visitedViews.slice(-1)[0]
      if (latestView) {
        this.$router.push(latestView.fullPath)
      }
    },
    openMenu(tag, e) {
      // Context menu functionality can be added here
    }
  }
}
</script>

<style lang="scss" scoped>
.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12);

  .tags-view-item {
    display: inline-block;
    position: relative;
    cursor: pointer;
    height: 26px;
    line-height: 26px;
    border: 1px solid #d8dce5;
    color: #495057;
    background: #fff;
    padding: 0 8px;
    font-size: 12px;
    margin-left: 5px;
    margin-top: 4px;
    border-radius: 2px;

    &:first-of-type {
      margin-left: 15px;
    }

    &:last-of-type {
      margin-right: 15px;
    }

    &.active {
      background-color: #42b983;
      color: #fff;
      border-color: #42b983;

      &::before {
        content: '';
        background: #fff;
        display: inline-block;
        width: 8px;
        height: 8px;
        border-radius: 50%;
        position: relative;
        margin-right: 2px;
      }
    }

    .el-icon-close {
      width: 16px;
      height: 16px;
      vertical-align: 2px;
      border-radius: 50%;
      text-align: center;
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
      transform-origin: 100% 50%;

      &:before {
        transform: scale(0.6);
        display: inline-block;
        vertical-align: -3px;
      }

      &:hover {
        background-color: #b4bccc;
        color: #fff;
      }
    }
  }
}
</style>
