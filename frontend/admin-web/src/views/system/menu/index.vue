<template>
  <div class="menu-container">
    <el-card>
      <div slot="header">
        <span>菜单管理</span>
        <el-button type="primary" size="small" style="float: right;" @click="handleAdd">新增菜单</el-button>
      </div>

      <el-table v-loading="loading" :data="menuList" row-key="id" border default-expand-all>
        <el-table-column prop="name" label="菜单名称" width="200" />
        <el-table-column prop="icon" label="图标" width="80">
          <template slot-scope="scope">
            <i v-if="scope.row.icon" :class="'el-icon-' + scope.row.icon"></i>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 1" type="success">目录</el-tag>
            <el-tag v-else-if="scope.row.type === 2">菜单</el-tag>
            <el-tag v-else type="info">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" />
        <el-table-column prop="component" label="组件路径" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" @click="handleAddChild(scope.row)">新增子菜单</el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <el-input v-model="form.parentName" placeholder="请选择上级菜单" readonly>
                <el-button slot="append" icon="el-icon-close" @click="form.parentId = 0" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio :label="1">目录</el-radio>
                <el-radio :label="2">菜单</el-radio>
                <el-radio :label="3">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路由路径" prop="path">
              <el-input v-model="form.path" placeholder="请输入路由路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.type !== 1" label="组件路径">
              <el-input v-model="form.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sort">
              <el-input-number v-model="form.sort" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Menu',
  data() {
    return {
      loading: false,
      menuList: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: undefined,
        parentId: 0,
        parentName: '顶级菜单',
        name: '',
        path: '',
        component: '',
        icon: '',
        sort: 0,
        type: 1
      },
      rules: {
        name: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      // 模拟数据
      setTimeout(() => {
        this.menuList = [
          {
            id: 1,
            name: '系统管理',
            icon: 'setting',
            type: 1,
            path: '/system',
            component: '',
            sort: 1,
            children: [
              { id: 2, name: '用户管理', icon: 'user', type: 2, path: '/system/user', component: 'system/user/index', sort: 1 },
              { id: 3, name: '角色管理', icon: 'peoples', type: 2, path: '/system/role', component: 'system/role/index', sort: 2 },
              { id: 4, name: '菜单管理', icon: 'menu', type: 2, path: '/system/menu', component: 'system/menu/index', sort: 3 }
            ]
          },
          {
            id: 5,
            name: 'Dashboard',
            icon: 'dashboard',
            type: 1,
            path: '/dashboard',
            component: '',
            sort: 0,
            children: [
              { id: 6, name: '工作台', icon: 'dashboard', type: 2, path: '/dashboard/workplace', component: 'dashboard/workplace', sort: 1 }
            ]
          }
        ]
        this.loading = false
      }, 300)
    },
    handleAdd() {
      this.dialogTitle = '新增菜单'
      this.dialogVisible = true
      this.form = {
        id: undefined,
        parentId: 0,
        parentName: '顶级菜单',
        name: '',
        path: '',
        component: '',
        icon: '',
        sort: 0,
        type: 1
      }
    },
    handleAddChild(row) {
      this.dialogTitle = '新增子菜单'
      this.dialogVisible = true
      this.form = {
        id: undefined,
        parentId: row.id,
        parentName: row.name,
        name: '',
        path: '',
        component: '',
        icon: '',
        sort: 0,
        type: 2
      }
    },
    handleEdit(row) {
      this.dialogTitle = '编辑菜单'
      this.dialogVisible = true
      this.form = { ...row, parentName: '顶级菜单' }
    },
    handleDelete(row) {
      this.$confirm('确认删除该菜单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getList()
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message.success('操作成功')
          this.dialogVisible = false
          this.getList()
        }
      })
    },
    cancel() {
      this.dialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.menu-container {
  .el-table {
    margin-top: 10px;
  }
}
</style>
