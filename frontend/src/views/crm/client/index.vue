<template>
  <div class="client-container">
    <div class="content-card">
      <div class="card-header">
        <h3>客户列表</h3>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增客户</el-button>
      </div>

      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="公司名称">
          <el-input v-model="queryForm.name" placeholder="请输入公司名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="行业">
          <el-select v-model="queryForm.industry" placeholder="请选择行业" clearable>
            <el-option label="互联网" value="互联网"></el-option>
            <el-option label="金融" value="金融"></el-option>
            <el-option label="教育" value="教育"></el-option>
            <el-option label="医疗" value="医疗"></el-option>
            <el-option label="房地产" value="房地产"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1"></el-option>
            <el-option label="公海池" :value="2"></el-option>
            <el-option label="禁用" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="name" label="公司名称" min-width="150"></el-table-column>
        <el-table-column prop="industry" label="行业" width="100"></el-table-column>
        <el-table-column prop="scale" label="规模" width="100"></el-table-column>
        <el-table-column prop="level" label="等级" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.level" :type="getLevelType(scope.row.level)">
              {{ scope.row.level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="活跃度" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isActive === 1 ? 'success' : 'info'">
              {{ scope.row.isActive === 1 ? '活跃' : '不活跃' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" @close="handleDialogClose">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司名称" prop="name">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行业" prop="industry">
              <el-select v-model="form.industry" placeholder="请选择行业" style="width: 100%">
                <el-option label="互联网" value="互联网"></el-option>
                <el-option label="金融" value="金融"></el-option>
                <el-option label="教育" value="教育"></el-option>
                <el-option label="医疗" value="医疗"></el-option>
                <el-option label="房地产" value="房地产"></el-option>
                <el-option label="其他" value="其他"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="融资阶段">
              <el-select v-model="form.financingStage" placeholder="请选择" style="width: 100%">
                <el-option label="不需要融资" value="不需要融资"></el-option>
                <el-option label="天使轮" value="天使轮"></el-option>
                <el-option label="A轮" value="A轮"></el-option>
                <el-option label="B轮" value="B轮"></el-option>
                <el-option label="C轮及以上" value="C轮及以上"></el-option>
                <el-option label="已上市" value="已上市"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规模">
              <el-select v-model="form.scale" placeholder="请选择规模" style="width: 100%">
                <el-option label="0-20人" value="0-20人"></el-option>
                <el-option label="20-50人" value="20-50人"></el-option>
                <el-option label="50-100人" value="50-100人"></el-option>
                <el-option label="100-500人" value="100-500人"></el-option>
                <el-option label="500人以上" value="500人以上"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户等级">
              <el-select v-model="form.level" placeholder="请选择等级" style="width: 100%">
                <el-option label="A级" value="A"></el-option>
                <el-option label="B级" value="B"></el-option>
                <el-option label="C级" value="C"></el-option>
                <el-option label="D级" value="D"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户来源">
              <el-input v-model="form.source"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="公司地址">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
        <el-form-item label="官网">
          <el-input v-model="form.website"></el-input>
        </el-form-item>
        <el-form-item label="公司简介">
          <el-input v-model="form.description" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="2">公海池</el-radio>
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
import { getClientList, addClient, updateClient, deleteClient } from '@/api/client'

export default {
  name: 'Client',
  data() {
    return {
      queryForm: {
        name: '',
        industry: '',
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
        name: '',
        industry: '',
        financingStage: '',
        scale: '',
        address: '',
        website: '',
        description: '',
        source: '',
        level: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入公司名称', trigger: 'blur' }]
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
        size: this.pagination.size
      }
      if (this.queryForm.name) params.name = this.queryForm.name
      if (this.queryForm.industry) params.industry = this.queryForm.industry
      if (this.queryForm.status !== '') params.status = this.queryForm.status
      getClientList(params).then(response => {
        if (response.data) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      })
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm = {
        name: '',
        industry: '',
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
      this.dialogTitle = '新增客户'
      this.form = {
        id: null,
        name: '',
        industry: '',
        financingStage: '',
        scale: '',
        address: '',
        website: '',
        description: '',
        source: '',
        level: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑客户'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该客户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteClient(row.id).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const action = this.form.id ? updateClient : addClient
          action(this.form).then(() => {
            this.$message.success(this.form.id ? '更新成功' : '新增成功')
            this.dialogVisible = false
            this.loadData()
          })
        }
      })
    },
    handleDialogClose() {
      this.$refs.form.resetFields()
    },
    getStatusType(status) {
      const types = { 0: 'danger', 1: 'success', 2: 'warning' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '禁用', 1: '正常', 2: '公海池' }
      return texts[status] || '未知'
    },
    getLevelType(level) {
      const types = { 'A': 'danger', 'B': 'warning', 'C': 'primary', 'D': 'info' }
      return types[level] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.client-container {
  .search-form {
    margin-bottom: 20px;
  }
}
</style>
