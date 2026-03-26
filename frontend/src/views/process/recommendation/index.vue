<template>
  <div class="recommendation-container">
    <div class="content-card">
      <div class="card-header">
        <h3>推荐记录</h3>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增推荐</el-button>
      </div>

      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="待推荐" value="PENDING"></el-option>
            <el-option label="简历筛选中" value="SCREENING"></el-option>
            <el-option label="面试中" value="INTERVIEW"></el-option>
            <el-option label="Offer谈判" value="OFFER"></el-option>
            <el-option label="入职中" value="ONBOARDING"></el-option>
            <el-option label="已入职" value="ONBOARDED"></el-option>
            <el-option label="已淘汰" value="REJECTED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="jobTitle" label="职位" min-width="150"></el-table-column>
        <el-table-column prop="candidateName" label="候选人" width="100"></el-table-column>
        <el-table-column label="当前状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="offerSalary" label="Offer薪资" width="100">
          <template slot-scope="scope">
            {{ scope.row.offerSalary ? (scope.row.offerSalary/10000).toFixed(0) + '万' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="interviewTime" label="面试时间" width="160"></el-table-column>
        <el-table-column prop="onboardDate" label="入职日期" width="120"></el-table-column>
        <el-table-column prop="createTime" label="推荐时间" width="160"></el-table-column>
        <el-table-column label="操作" fixed="right" width="250">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="primary" @click="handleUpdateStatus(scope.row)">状态流转</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" @close="handleDialogClose">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="职位" prop="jobId">
          <el-select v-model="form.jobId" placeholder="请选择职位" style="width: 100%" filterable>
            <el-option v-for="job in jobList" :key="job.id" :label="job.title" :value="job.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="候选人" prop="candidateId">
          <el-select v-model="form.candidateId" placeholder="请选择候选人" style="width: 100%" filterable>
            <el-option v-for="candidate in candidateList" :key="candidate.id" :label="candidate.name + ' - ' + candidate.currentCompany" :value="candidate.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 状态流转对话框 -->
    <el-dialog title="状态流转" :visible.sync="statusDialogVisible" width="500px">
      <el-form label-width="100px">
        <el-form-item label="当前状态">
          <el-tag :type="getStatusType(currentStatus)">{{ getStatusText(currentStatus) }}</el-tag>
        </el-form-item>
        <el-form-item label="流转到">
          <el-select v-model="newStatus" placeholder="请选择新状态" style="width: 100%">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="statusRemark" type="textarea" :rows="2" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="statusDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmStatusChange">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getRecommendationList, addRecommendation, updateRecommendation, deleteRecommendation, updateRecommendationStatus } from '@/api/recommendation'
import { getJobList } from '@/api/job'
import { getCandidateList } from '@/api/candidate'

export default {
  name: 'Recommendation',
  data() {
    return {
      queryForm: {
        status: ''
      },
      tableData: [],
      jobList: [],
      candidateList: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        jobId: null,
        candidateId: null,
        remark: ''
      },
      rules: {
        jobId: [{ required: true, message: '请选择职位', trigger: 'change' }],
        candidateId: [{ required: true, message: '请选择候选人', trigger: 'change' }]
      },
      // 状态流转相关
      statusDialogVisible: false,
      currentRow: null,
      currentStatus: '',
      newStatus: '',
      statusRemark: '',
      statusOptions: [
        { value: 'PENDING', label: '待推荐' },
        { value: 'SCREENING', label: '简历筛选中' },
        { value: 'INTERVIEW', label: '面试中' },
        { value: 'OFFER', label: 'Offer谈判中' },
        { value: 'ONBOARDING', label: '入职中' },
        { value: 'ONBOARDED', label: '已入职' },
        { value: 'REJECTED', label: '已淘汰' }
      ]
    }
  },
  mounted() {
    this.loadData()
    this.loadJobList()
    this.loadCandidateList()
  },
  methods: {
    loadData() {
      const params = {
        current: this.pagination.current,
        size: this.pagination.size,
        ...this.queryForm
      }
      getRecommendationList(params).then(response => {
        if (response.data) {
          this.tableData = response.data || []
          this.pagination.total = response.total || 0
        }
      })
    },
    loadJobList() {
      getJobList({ current: 1, size: 1000 }).then(response => {
        if (response.data) {
          this.jobList = response.data || []
        }
      })
    },
    loadCandidateList() {
      getCandidateList({ current: 1, size: 1000 }).then(response => {
        if (response.data) {
          this.candidateList = response.data || []
        }
      })
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm = { status: '' }
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
      this.dialogTitle = '新增推荐'
      this.form = {
        id: null,
        jobId: null,
        candidateId: null,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑推荐'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该推荐记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRecommendation(row.id).then(response => {
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
          const action = this.form.id ? updateRecommendation : addRecommendation
          action(this.form).then(response => {
            if (response.success) {
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
    },
    handleUpdateStatus(row) {
      this.currentRow = row
      this.currentStatus = row.status
      this.newStatus = ''
      this.statusRemark = ''
      this.statusDialogVisible = true
    },
    confirmStatusChange() {
      if (!this.newStatus) {
        this.$message.warning('请选择新状态')
        return
      }
      updateRecommendationStatus(this.currentRow.id, this.newStatus).then(response => {
        if (response.success) {
          this.$message.success('状态更新成功')
          this.statusDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '操作失败')
        }
      })
    },
    getStatusType(status) {
      const types = {
        'PENDING': 'info',
        'SCREENING': 'warning',
        'INTERVIEW': 'primary',
        'OFFER': 'warning',
        'ONBOARDING': 'success',
        'ONBOARDED': 'success',
        'REJECTED': 'danger'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        'PENDING': '待推荐',
        'SCREENING': '简历筛选中',
        'INTERVIEW': '面试中',
        'OFFER': 'Offer谈判中',
        'ONBOARDING': '入职中',
        'ONBOARDED': '已入职',
        'REJECTED': '已淘汰'
      }
      return texts[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.recommendation-container {
  .search-form {
    margin-bottom: 20px;
  }
}
</style>
