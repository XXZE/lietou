<template>
  <div class="job-container">
    <div class="content-card">
      <div class="card-header">
        <h3>职位列表</h3>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">发布职位</el-button>
      </div>

      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="职位名称">
          <el-input v-model="queryForm.title" placeholder="请输入职位名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="进行中" :value="1"></el-option>
            <el-option label="暂停" :value="2"></el-option>
            <el-option label="已关闭" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="紧急程度">
          <el-select v-model="queryForm.urgencyLevel" placeholder="请选择" clearable>
            <el-option label="P0-紧急" :value="0"></el-option>
            <el-option label="P1-高" :value="1"></el-option>
            <el-option label="P2-普通" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="title" label="职位名称" min-width="150"></el-table-column>
        <el-table-column prop="clientName" label="客户" width="120"></el-table-column>
        <el-table-column prop="department" label="部门" width="120"></el-table-column>
        <el-table-column prop="location" label="工作地点" width="100"></el-table-column>
        <el-table-column label="薪资范围" width="120">
          <template slot-scope="scope">
            {{ scope.row.salaryMin ? `${(scope.row.salaryMin/10000).toFixed(0)}-${(scope.row.salaryMax/10000).toFixed(0)}万` : '面议' }}
          </template>
        </el-table-column>
        <el-table-column prop="urgencyLevel" label="紧急程度" width="100">
          <template slot-scope="scope">
            <el-tag :type="getUrgencyType(scope.row.urgencyLevel)">
              {{ getUrgencyText(scope.row.urgencyLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="进度" width="180">
          <template slot-scope="scope">
            <el-progress :percentage="getProgress(scope.row)" :status="getProgressStatus(scope.row)"></el-progress>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" @close="handleDialogClose">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位名称" prop="title">
              <el-input v-model="form.title"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户" prop="clientId">
              <el-select v-model="form.clientId" placeholder="请选择客户" style="width: 100%" filterable>
                <el-option v-for="client in clientList" :key="client.id" :label="client.name" :value="client.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门">
              <el-input v-model="form.department"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工作地点">
              <el-input v-model="form.location"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="薪资下限">
              <el-input v-model="form.salaryMin" type="number" placeholder="年薪(万)">
                <template slot="append">万</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="薪资上限">
              <el-input v-model="form.salaryMax" type="number" placeholder="年薪(万)">
                <template slot="append">万</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="招聘人数">
              <el-input-number v-model="form.headcount" :min="1" :max="100"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="紧急程度">
              <el-select v-model="form.urgencyLevel" placeholder="请选择" style="width: 100%">
                <el-option label="P0-紧急" :value="0"></el-option>
                <el-option label="P1-高" :value="1"></el-option>
                <el-option label="P2-普通" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" placeholder="请选择" style="width: 100%">
                <el-option label="进行中" :value="1"></el-option>
                <el-option label="暂停" :value="2"></el-option>
                <el-option label="已关闭" :value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="职位标签">
          <el-input v-model="form.tags" placeholder="多个标签用逗号分隔，如: Java,架构师,5年经验"></el-input>
        </el-form-item>
        <el-form-item label="职位JD">
          <el-input v-model="form.jobDescription" type="textarea" :rows="4" placeholder="请输入职位描述"></el-input>
        </el-form-item>
        <el-form-item label="硬性要求">
          <el-input v-model="form.requirements" type="textarea" :rows="3" placeholder="请输入硬性要求"></el-input>
        </el-form-item>
        <el-form-item label="面试官">
          <el-input v-model="form.interviewer" placeholder="请输入面试官信息"></el-input>
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
import { getJobList, addJob, updateJob, deleteJob } from '@/api/job'
import { getClientList } from '@/api/client'

export default {
  name: 'Job',
  data() {
    return {
      queryForm: {
        title: '',
        status: '',
        urgencyLevel: ''
      },
      tableData: [],
      clientList: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        title: '',
        clientId: null,
        department: '',
        location: '',
        salaryMin: null,
        salaryMax: null,
        headcount: 1,
        urgencyLevel: 2,
        status: 1,
        tags: '',
        jobDescription: '',
        requirements: '',
        interviewer: ''
      },
      rules: {
        title: [{ required: true, message: '请输入职位名称', trigger: 'blur' }],
        clientId: [{ required: true, message: '请选择客户', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadClientList()
  },
  methods: {
    loadData() {
      const params = {
        current: this.pagination.current,
        size: this.pagination.size,
        ...this.queryForm
      }
      getJobList(params).then(response => {
        if (response.data) {
          this.tableData = response.data || []
          this.pagination.total = response.total || 0
        }
      })
    },
    loadClientList() {
      getClientList({ current: 1, size: 1000 }).then(response => {
        if (response.data) {
          this.clientList = response.data || []
        }
      })
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm = {
        title: '',
        status: '',
        urgencyLevel: ''
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
      this.dialogTitle = '发布职位'
      this.form = {
        id: null,
        title: '',
        clientId: null,
        department: '',
        location: '',
        salaryMin: null,
        salaryMax: null,
        headcount: 1,
        urgencyLevel: 2,
        status: 1,
        tags: '',
        jobDescription: '',
        requirements: '',
        interviewer: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑职位'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该职位吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteJob(row.id).then(response => {
          if (response.success) {
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
          // 薪资单位转换：万->元
          const submitData = { ...this.form }
          if (submitData.salaryMin) submitData.salaryMin = submitData.salaryMin * 10000
          if (submitData.salaryMax) submitData.salaryMax = submitData.salaryMax * 10000

          const action = this.form.id ? updateJob : addJob
          action(submitData).then(response => {
            if (response.success) {
              this.$message.success(this.form.id ? '更新成功' : '发布成功')
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
    },
    getStatusType(status) {
      const types = { 1: 'success', 2: 'warning', 3: 'info' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 1: '进行中', 2: '暂停', 3: '已关闭' }
      return texts[status] || '未知'
    },
    getUrgencyType(level) {
      const types = { 0: 'danger', 1: 'warning', 2: 'primary' }
      return types[level] || 'info'
    },
    getUrgencyText(level) {
      const texts = { 0: 'P0-紧急', 1: 'P1-高', 2: 'P2-普通' }
      return texts[level] || '未知'
    },
    getProgress(row) {
      if (row.headcount === 0) return 0
      return Math.round((row.onboardCount / row.headcount) * 100)
    },
    getProgressStatus(row) {
      if (row.onboardCount >= row.headcount) return 'success'
      if (row.interviewCount > 0) return 'warning'
      return null
    }
  }
}
</script>

<style lang="scss" scoped>
.job-container {
  .search-form {
    margin-bottom: 20px;
  }
}
</style>
