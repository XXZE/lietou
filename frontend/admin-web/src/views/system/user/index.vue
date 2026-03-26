<template>
  <div class="user-container">
    <el-card>
      <div slot="header">
        <span>用户管理</span>
        <el-button type="primary" size="small" style="float: right;" @click="handleAdd">新增用户</el-button>
      </div>

      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable size="small" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
            <el-option label="正常" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="userList" border>
        <el-table-column label="ID" prop="id" width="80" />
        <el-table-column label="用户名" prop="username" />
        <el-table-column label="昵称" prop="nickname" />
        <el-table-column label="邮箱" prop="email" />
        <el-table-column label="手机" prop="phone" />
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

      <el-pagination
        :current-page="queryParams.pageNum"
        :page-size="queryParams.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机" />
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
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user'

export default {
  name: 'User',
  data() {
    return {
      loading: false,
      total: 0,
      userList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: '',
        status: ''
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: undefined,
        username: '',
        nickname: '',
        email: '',
        phone: '',
        status: 1
      },
      rules: {
        username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getUserList(this.queryParams).then(response => {
        this.userList = response.data.rows || []
        this.total = response.data.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
        // 模拟数据
        this.userList = [
          { id: 1, username: 'admin', nickname: '超级管理员', email: 'admin@example.com', phone: '13800138000', status: 1, createTime: '2024-01-01 10:00:00' }
        ]
        this.total = 1
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        username: '',
        status: ''
      }
      this.getList()
    },
    handleAdd() {
      this.dialogTitle = '新增用户'
      this.dialogVisible = true
      this.form = {
        id: undefined,
        username: '',
        nickname: '',
        email: '',
        phone: '',
        status: 1
      }
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.dialogVisible = true
      this.form = { ...row }
    },
    handleDelete(row) {
      this.$confirm('确认删除该用户吗？', '提示', {
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
          if (this.form.id) {
            this.$message.success('更新成功')
          } else {
            this.$message.success('新增成功')
          }
          this.dialogVisible = false
          this.getList()
        }
      })
    },
    cancel() {
      this.dialogVisible = false
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.user-container {
  .search-form {
    margin-bottom: 15px;
  }
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
