<template>
  <div class="role-container">
    <div class="content-card">
      <div class="card-header">
        <h3>角色列表</h3>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增角色</el-button>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="code" label="角色编码" width="200"></el-table-column>
        <el-table-column prop="name" label="角色名称" width="200"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
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
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" :disabled="form.id ? true : false"></el-input>
        </el-form-item>
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
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
import { getRoleList, addRole, updateRole, deleteRole } from '@/api/role'

export default {
  name: 'Role',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        code: '',
        name: '',
        status: 1
      },
      rules: {
        code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      getRoleList().then(response => {
        if (response.code === 200) {
          this.tableData = response.data || []
        }
      })
    },
    handleAdd() {
      this.dialogTitle = '新增角色'
      this.form = {
        id: null,
        code: '',
        name: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该角色吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRole(row.id).then(response => {
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
          const action = this.form.id ? updateRole : addRole
          action(this.form).then(response => {
            if (response.code === 200) {
              this.$message.success(this.form.id ? '更新成功' : '新增成功')
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
.role-container {
  // Styles
}
</style>
