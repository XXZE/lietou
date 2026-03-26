<template>
  <div class="tag-container">
    <div class="content-card">
      <div class="card-header">
        <h3>标签管理</h3>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增标签</el-button>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="name" label="标签名称" width="150"></el-table-column>
        <el-table-column prop="color" label="颜色" width="100">
          <template slot-scope="scope">
            <el-color-picker v-model="scope.row.color" disabled size="mini"></el-color-picker>
            <span style="margin-left: 10px">{{ scope.row.color }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120"></el-table-column>
        <el-table-column prop="sort" label="排序" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
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
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="handleDialogClose">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="颜色">
          <el-color-picker v-model="form.color"></el-color-picker>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="学历" value="学历"></el-option>
            <el-option label="背景" value="背景"></el-option>
            <el-option label="状态" value="状态"></el-option>
            <el-option label="技术" value="技术"></el-option>
            <el-option label="职位" value="职位"></el-option>
            <el-option label="经验" value="经验"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="999"></el-input-number>
        </el-form-item>
        <el-form-item label="状态">
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
import { getTagList, addTag, updateTag, deleteTag } from '@/api/tag'

export default {
  name: 'Tag',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        name: '',
        color: '#409EFF',
        category: '',
        sort: 0,
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      getTagList().then(response => {
        if (response.data) {
          this.tableData = response.data || []
        }
      })
    },
    handleAdd() {
      this.dialogTitle = '新增标签'
      this.form = {
        id: null,
        name: '',
        color: '#409EFF',
        category: '',
        sort: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑标签'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该标签吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTag(row.id).then(response => {
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
          const action = this.form.id ? updateTag : addTag
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
    }
  }
}
</script>

<style lang="scss" scoped>
.tag-container {
  .content-card {
    margin-bottom: 20px;
  }
}
</style>
