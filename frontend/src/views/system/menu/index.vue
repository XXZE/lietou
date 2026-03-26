<template>
  <div class="menu-container">
    <div class="content-card">
      <div class="card-header">
        <h3>菜单列表</h3>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增菜单</el-button>
      </div>

      <el-table :data="tableData" border stripe row-key="id" default-expand-all style="width: 100%">
        <el-table-column prop="name" label="菜单名称" width="200"></el-table-column>
        <el-table-column prop="icon" label="图标" width="100">
          <template slot-scope="scope">
            <i :class="scope.row.icon" v-if="scope.row.icon"></i>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" width="200"></el-table-column>
        <el-table-column prop="component" label="组件路径" width="200"></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 1" type="primary">目录</el-tag>
            <el-tag v-else-if="scope.row.type === 2" type="success">菜单</el-tag>
            <el-tag v-else type="warning">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" @close="handleDialogClose">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="父级菜单">
          <el-cascader
            v-model="form.parentId"
            :options="menuOptions"
            :props="{ checkStrictly: true, label: 'name', value: 'id' }"
            clearable
            placeholder="请选择父级菜单"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-radio-group v-model="form.type">
            <el-radio :label="1">目录</el-radio>
            <el-radio :label="2">菜单</el-radio>
            <el-radio :label="3">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="form.path"></el-input>
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="form.component"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.icon" placeholder="例如: el-icon-user"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="999"></el-input-number>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">显示</el-radio>
            <el-radio :label="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getMenuList, addMenu, updateMenu, deleteMenu } from '@/api/menu'

export default {
  name: 'Menu',
  data() {
    return {
      tableData: [],
      menuOptions: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        parentId: 0,
        name: '',
        path: '',
        component: '',
        icon: '',
        type: 1,
        sort: 0,
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
        path: [{ required: true, message: '请输入路由路径', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      getMenuList().then(response => {
        if (response.code === 200) {
          this.tableData = this.buildTree(response.data || [])
          this.menuOptions = [{ id: 0, name: '顶级菜单' }, ...response.data || []]
        }
      })
    },
    buildTree(data) {
      const result = []
      const map = {}
      data.forEach(item => {
        map[item.id] = { ...item }
      })
      data.forEach(item => {
        const parent = map[item.parentId]
        if (parent) {
          if (!parent.children) {
            parent.children = []
          }
          parent.children.push(map[item.id])
        } else {
          result.push(map[item.id])
        }
      })
      return result
    },
    handleAdd() {
      this.dialogTitle = '新增菜单'
      this.form = {
        id: null,
        parentId: 0,
        name: '',
        path: '',
        component: '',
        icon: '',
        type: 1,
        sort: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑菜单'
      this.form = { ...row, parentId: row.parentId || 0 }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该菜单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenu(row.id).then(response => {
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const formData = { ...this.form }
          if (formData.parentId && formData.parentId.length > 0) {
            formData.parentId = formData.parentId[formData.parentId.length - 1]
          }
          const action = formData.id ? updateMenu : addMenu
          action(formData).then(response => {
            if (response.code === 200) {
              this.$message.success(formData.id ? '更新成功' : '新增成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(response.message || '操作失败')
            }
          })
        }
      })
    },
    handleDialogClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
.menu-container {
  // Styles
}
</style>
