<template>
  <div class="candidate-container">
    <div class="content-card">
      <div class="card-header">
        <h3>候选人列表</h3>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增候选人</el-button>
      </div>

      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="queryForm.name" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="公司">
          <el-input v-model="queryForm.currentCompany" placeholder="请输入公司名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1"></el-option>
            <el-option label="已入职" :value="2"></el-option>
            <el-option label="已淘汰" :value="3"></el-option>
            <el-option label="黑名单" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="gender" label="性别" width="60">
          <template slot-scope="scope">
            {{ scope.row.gender === 1 ? '男' : (scope.row.gender === 0 ? '女' : '-') }}
          </template>
        </el-table-column>
        <el-table-column prop="currentCompany" label="当前公司" min-width="120"></el-table-column>
        <el-table-column prop="currentPosition" label="当前职位" width="120"></el-table-column>
        <el-table-column prop="workYears" label="工作年限" width="80"></el-table-column>
        <el-table-column prop="education" label="学历" width="80"></el-table-column>
        <el-table-column prop="location" label="所在地" width="100"></el-table-column>
        <el-table-column prop="phone" label="电话" width="120">
          <template slot-scope="scope">
            {{ maskPhone(scope.row.phone) }}
          </template>
        </el-table-column>
        <el-table-column label="简历" width="100" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.resumeUrl"
              type="text"
              icon="el-icon-document"
              @click="handleViewResume(scope.row.resumeUrl)">
              查看简历
            </el-button>
            <span v-else style="color: #999;">无</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" @close="handleDialogClose" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="出生日期">
              <el-date-picker v-model="form.birthday" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入11位手机号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱地址"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="当前公司">
              <el-input v-model="form.currentCompany"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前职位">
              <el-input v-model="form.currentPosition"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="当前年薪">
              <el-input-number v-model="form.currentSalary" :min="0" :max="1000" :precision="0" :controls="false" style="width: 100%" placeholder="万">
                <template slot="append">万</template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="期望年薪">
              <el-input-number v-model="form.expectedSalary" :min="0" :max="1000" :precision="0" :controls="false" style="width: 100%" placeholder="万">
                <template slot="append">万</template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="工作年限">
              <el-input-number v-model="form.workYears" :min="0" :max="50" :controls="false" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学历">
              <el-select v-model="form.education" placeholder="请选择学历" style="width: 100%">
                <el-option label="初中及以下" value="初中及以下"></el-option>
                <el-option label="高中/中专" value="高中/中专"></el-option>
                <el-option label="大专" value="大专"></el-option>
                <el-option label="本科" value="本科"></el-option>
                <el-option label="硕士" value="硕士"></el-option>
                <el-option label="博士" value="博士"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在地">
              <el-input v-model="form.location"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="候选人来源">
              <el-input v-model="form.source" placeholder="如: 猎聘,前程无忧,朋友推荐"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="正常" :value="1"></el-option>
                <el-option label="已入职" :value="2"></el-option>
                <el-option label="已淘汰" :value="3"></el-option>
                <el-option label="黑名单" :value="4"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="简历上传">
          <el-upload
            class="resume-upload"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-remove="handleRemoveFile"
            :file-list="fileList"
            :limit="1"
            :auto-upload="true"
            :show-file-list="true"
            accept=".pdf,.doc,.docx"
            :before-upload="beforeUpload">
            <el-button size="small" icon="el-icon-upload">上传简历</el-button>
            <div slot="tip" class="el-upload__tip">支持 PDF、Word 格式，文件大小不超过 5MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="简历链接">
          <el-input v-model="form.resumeUrl" placeholder="请输入简历URL" readonly></el-input>
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

    <el-dialog title="查看简历" :visible.sync="resumeDialogVisible" width="80%" top="5vh" :close-on-click-modal="false">
      <div v-if="resumeUrl" class="resume-preview-wrapper">
        <div class="resume-toolbar">
          <el-button type="primary" size="small" @click="handleDownloadResume" icon="el-icon-download">
            下载简历
          </el-button>
        </div>
        <div class="resume-content">
          <!-- PDF文件使用iframe预览 -->
          <iframe
            v-if="resumeFileExt === 'pdf'"
            :src="resumeUrl"
            frameborder="0"
            class="resume-iframe">
          </iframe>
          <!-- Word文件显示提示 -->
          <div v-else class="word-preview-tip">
            <i class="el-icon-document" style="font-size: 80px; color: #409EFF; margin-bottom: 20px;"></i>
            <p style="font-size: 18px; margin-bottom: 10px;">Word文档暂不支持在线预览</p>
            <p style="color: #909399; font-size: 14px; margin-bottom: 20px;">请点击右下角下载按钮下载后查看</p>
            <el-tag>{{ resumeFileExt.toUpperCase() }} 文档</el-tag>
          </div>
        </div>
      </div>
      <div v-else class="no-resume">
        <el-empty description="暂无简历"></el-empty>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resumeDialogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCandidateList, addCandidate, updateCandidate, deleteCandidate } from '@/api/candidate'

export default {
  name: 'Candidate',
  data() {
    return {
      queryForm: {
        name: '',
        currentCompany: '',
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
        gender: null,
        birthday: null,
        phone: '',
        email: '',
        currentCompany: '',
        currentPosition: '',
        currentSalary: null,
        expectedSalary: null,
        workYears: null,
        education: '',
        location: '',
        source: '',
        status: 1,
        resumeUrl: '',
        remark: ''
      },
      uploadUrl: '/api/file/upload',
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      },
      fileList: [],
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        email: [
          { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      },
      resumeDialogVisible: false,
      resumeUrl: '',
      resumeFileExt: ''
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
      if (this.queryForm.currentCompany) params.currentCompany = this.queryForm.currentCompany
      if (this.queryForm.status !== '') params.status = this.queryForm.status
      getCandidateList(params).then(response => {
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
        currentCompany: '',
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
      this.dialogTitle = '新增候选人'
      this.form = {
        id: null,
        name: '',
        gender: null,
        birthday: null,
        phone: '',
        email: '',
        currentCompany: '',
        currentPosition: '',
        currentSalary: null,
        expectedSalary: null,
        workYears: null,
        education: '',
        location: '',
        source: '',
        status: 1,
        resumeUrl: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑候选人'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该候选人吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCandidate(row.id).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 薪资单位转换：万->元
          const submitData = { ...this.form }
          if (submitData.currentSalary) submitData.currentSalary = submitData.currentSalary * 10000
          if (submitData.expectedSalary) submitData.expectedSalary = submitData.expectedSalary * 10000

          const action = this.form.id ? updateCandidate : addCandidate
          action(submitData).then(() => {
            this.$message.success(this.form.id ? '更新成功' : '新增成功')
            this.dialogVisible = false
            this.loadData()
          })
        }
      })
    },
    handleDialogClose() {
      this.$refs.form.resetFields()
      this.fileList = []
    },
    beforeUpload(file) {
      const isDoc = file.type === 'application/pdf' ||
                     file.type === 'application/msword' ||
                     file.type === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
      const isLt5M = file.size / 1024 / 1024 < 5

      if (!isDoc) {
        this.$message.error('只能上传 PDF、Word 格式的文件！')
        return false
      }
      if (!isLt5M) {
        this.$message.error('文件大小不能超过 5MB！')
        return false
      }
      return true
    },
    handleUploadSuccess(response, file, fileList) {
      console.log('上传成功响应:', response)
      if (response.code === 200) {
        this.form.resumeUrl = response.data.url
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    handleUploadError(err, file, fileList) {
      console.error('上传失败:', err)
      this.$message.error('文件上传失败: ' + (err.message || '未知错误'))
    },
    handleRemoveFile(file, fileList) {
      this.form.resumeUrl = ''
    },
    getStatusType(status) {
      const types = { 1: 'success', 2: 'primary', 3: 'info', 4: 'danger' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 1: '正常', 2: '已入职', 3: '已淘汰', 4: '黑名单' }
      return texts[status] || '未知'
    },
    maskPhone(phone) {
      if (!phone) return '-'
      return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
    },
    getFileExtension(url) {
      if (!url) return ''
      const parts = url.split('.')
      return parts.length > 1 ? parts[parts.length - 1].toLowerCase() : ''
    },
    handleViewResume(url) {
      // 确保URL以/api开头,通过Vue代理访问
      if (url && !url.startsWith('/api')) {
        this.resumeUrl = '/api' + url
      } else {
        this.resumeUrl = url
      }
      this.resumeFileExt = this.getFileExtension(url)
      this.resumeDialogVisible = true
    },
    handleDownloadResume() {
      if (this.resumeUrl) {
        window.open(this.resumeUrl, '_blank')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.candidate-container {
  .search-form {
    margin-bottom: 20px;
  }

  .resume-upload {
    ::v-deep .el-upload-list {
      margin-top: 10px;
    }

    ::v-deep .el-upload-list__item-name {
      max-width: 400px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .resume-preview-wrapper {
    position: relative;
    width: 100%;
    height: 70vh;

    .resume-toolbar {
      position: absolute;
      bottom: 0;
      right: 0;
      z-index: 10;
      padding: 15px;
      background: rgba(255, 255, 255, 0.95);
      border-top: 1px solid #dcdfe6;
      border-radius: 8px 0 0 0;
      box-shadow: -2px -2px 8px rgba(0, 0, 0, 0.1);
    }

    .resume-content {
      width: 100%;
      height: 100%;

      .resume-iframe {
        width: 100%;
        height: 100%;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
      }

      .word-preview-tip {
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        background: #f5f7fa;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
      }
    }
  }

  .no-resume {
    height: 70vh;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
