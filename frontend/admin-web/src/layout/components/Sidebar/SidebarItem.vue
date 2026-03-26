<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
      <el-menu-item :index="resolvePath(onlyOneChild.path)" :route="onlyOneChild">
        <i v-if="onlyOneChild.meta.icon" :class="'el-icon-' + onlyOneChild.meta.icon"></i>
        <span v-if="onlyOneChild.meta.title" slot="title">{{ onlyOneChild.meta.title }}</span>
      </el-menu-item>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template slot="title">
        <i v-if="item.meta.icon" :class="'el-icon-' + item.meta.icon"></i>
        <span v-if="item.meta.title">{{ item.meta.title }}</span>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :item="child"
        :base-path="resolvePath(child.path)"
      />
    </el-submenu>
  </div>
</template>

<script>
export default {
  name: 'SidebarItem',
  props: {
    item: {
      type: Object,
      required: true
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    this.onlyOneChild = null
    return {}
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          this.onlyOneChild = item
          return true
        }
      })

      if (showingChildren.length === 1) {
        return true
      }

      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath) {
      return routePath
    }
  }
}
</script>
