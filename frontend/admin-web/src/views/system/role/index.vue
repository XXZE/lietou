<template>
  <div class="role-container">
    <el-card>
      <div slot="header">
        <span>角色管理</span>
        <el-button type="primary" size="small" style="float: right;" @click="handleAdd">新增角色</el-button>
      </div>

      <el-table v-loading="loading" :data="roleList" border>
        <el-table-column label="ID" prop="id" width="80" />
        <el-table-column label="角色编码" prop="code" />
        <el-table-column label="角色名称" prop="name" />
        <el-table-column label="状态" prop="status" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="180" />
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
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
  name: 'Role',
  data() {
    return {
      loading: false,
      roleList: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: undefined,
        code: '',
        name: '',
        status: 1
      },
      rules: {
        code: [{ required: true, message: '角色编码不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }]
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
        this.roleList = [
          { id: 1, code: 'SUPER_ADMIN', name: '超级管理员', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 2, code: 'ADMIN', name: '系统管理员', status: 1, createTime: '2024-01-02 10:00:00' },
          { id: 3, code: 'USER', name: '普通用户', status: 1, createTime: '2024-01-03 10:00:00' }
        ]
        this.loading = false
      }, 300)
    },
    handleAdd() {
      this.dialogTitle = '新增角色'
      this.dialogVisible = true
      this.form = { id: undefined, code: '', name: '', status: 1 }
    },
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.dialogVisible = true
      this.form = { ...row }
    },
    handleDelete(row) {
      this.$confirm('确认删除该角色吗？', '提示', {
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
.role-container {
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
