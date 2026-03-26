<template>
  <div class="user-container">
    <div class="content-card">
      <div class="card-header">
        <h3>用户列表</h3>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增用户</el-button>
      </div>

      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="queryForm.username" placeholder="请输入用户名" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="1"></el-option>
            <el-option label="禁用" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="150"></el-table-column>
        <el-table-column prop="nickname" label="昵称" width="150"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="150"></el-table-column>
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

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
      >
      </el-pagination>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" @close="handleDialogClose">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="form.id ? true : false"></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone"></el-input>
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
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user'

export default {
  name: 'User',
  data() {
    return {
      queryForm: {
        username: '',
        status: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        username: '',
        nickname: '',
        email: '',
        phone: '',
        status: 1
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      const params = {
        current: this.pagination.current,
        size: this.pagination.size,
        ...this.queryForm
      }
      getUserList(params).then(response => {
        if (response.code === 200) {
          this.tableData = response.data || []
          this.pagination.total = response.total || 0
        }
      })
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm = {
        username: '',
        status: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },
    handleAdd() {
      this.dialogTitle = '新增用户'
      this.form = {
        id: null,
        username: '',
        nickname: '',
        email: '',
        phone: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser(row.id).then(response => {
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
          const action = this.form.id ? updateUser : addUser
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
.user-container {
  .search-form {
    margin-bottom: 20px;
  }
}
</style>
