<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-client">
            <i class="el-icon-office-building card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">客户数量</div>
            <div class="card-panel-num">{{ stats.clientCount || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-job">
            <i class="el-icon-briefcase card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">进行中职位</div>
            <div class="card-panel-num">{{ stats.jobCount || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-candidate">
            <i class="el-icon-user card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">候选人库</div>
            <div class="card-panel-num">{{ stats.candidateCount || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-recommend">
            <i class="el-icon-document card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">推荐总数</div>
            <div class="card-panel-num">{{ stats.recommendCount || 0 }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 待办任务和今日面试 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <div class="content-card">
          <div class="card-header">
            <h3>待办任务</h3>
          </div>
          <div class="todo-list">
            <div class="todo-item" v-for="(item, index) in todoList" :key="index">
              <div class="todo-icon" :style="{ background: item.color }">
                <i :class="item.icon"></i>
              </div>
              <div class="todo-content">
                <div class="todo-title">{{ item.title }}</div>
                <div class="todo-desc">{{ item.desc }}</div>
              </div>
              <div class="todo-count">{{ item.count }}</div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="content-card">
          <div class="card-header">
            <h3>今日面试安排</h3>
          </div>
          <div class="interview-list">
            <el-empty v-if="interviewList.length === 0" description="今日暂无面试安排"></el-empty>
            <div class="interview-item" v-for="(item, index) in interviewList" :key="index">
              <div class="interview-time">{{ item.time }}</div>
              <div class="interview-info">
                <div class="interview-title">{{ item.candidateName }} - {{ item.jobTitle }}</div>
                <div class="interview-type">{{ item.interviewType }} | {{ item.interviewer }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快捷操作和数据看板 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="8">
        <div class="content-card">
          <div class="card-header">
            <h3>快捷操作</h3>
          </div>
          <div class="quick-actions">
            <el-button type="primary" icon="el-icon-plus" @click="goTo('/crm/client')">新增客户</el-button>
            <el-button type="success" icon="el-icon-briefcase" @click="goTo('/crm/job')">发布职位</el-button>
            <el-button type="warning" icon="el-icon-user" @click="goTo('/talent/candidate')">录入候选人</el-button>
            <el-button type="info" icon="el-icon-document" @click="goTo('/process/recommendation')">推荐候选人</el-button>
          </div>
        </div>
      </el-col>
      <el-col :span="16">
        <div class="content-card">
          <div class="card-header">
            <h3>推荐转化漏斗</h3>
          </div>
          <div class="funnel-chart">
            <div class="funnel-item" v-for="(item, index) in funnelData" :key="index">
              <div class="funnel-label">{{ item.label }}</div>
              <div class="funnel-bar" :style="{ width: item.percentage + '%', background: item.color }">
                {{ item.value }}
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getClientList } from '@/api/client'
import { getJobList } from '@/api/job'
import { getCandidateList } from '@/api/candidate'
import { getRecommendationStatistics } from '@/api/recommendation'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        clientCount: 0,
        jobCount: 0,
        candidateCount: 0,
        recommendCount: 0
      },
      todoList: [
        { title: '待推荐候选人', desc: '已联系待推荐的候选人', icon: 'el-icon-document', color: '#409EFF', count: 0 },
        { title: '待反馈', desc: '等待HR反馈的候选人', icon: 'el-icon-bell', color: '#E6A23C', count: 0 },
        { title: '今日面试', desc: '今日需要参加的面试', icon: 'el-icon-date', color: '#67C23A', count: 0 },
        { title: '待跟进客户', desc: '需要跟进的客户', icon: 'el-icon-phone', color: '#F56C6C', count: 0 }
      ],
      interviewList: [],
      funnelData: [
        { label: '总推荐', value: 0, percentage: 100, color: '#409EFF' },
        { label: '面试', value: 0, percentage: 0, color: '#67C23A' },
        { label: 'Offer', value: 0, percentage: 0, color: '#E6A23C' },
        { label: '入职', value: 0, percentage: 0, color: '#F56C6C' }
      ],
      currentTime: '',
      timer: null
    }
  },
  computed: {
    username() {
      return this.$store.state.userInfo.nickname || this.$store.state.userInfo.username
    }
  },
  mounted() {
    this.loadStats()
    this.loadFunnelData()
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    loadStats() {
      // 加载客户数量
      getClientList({ current: 1, size: 1 }).then(response => {
        if (response.total) {
          this.stats.clientCount = response.total
          this.todoList[3].count = Math.ceil(response.total * 0.2)
        }
      })

      // 加载进行中职位数量
      getJobList({ current: 1, size: 1, status: 1 }).then(response => {
        if (response.total) {
          this.stats.jobCount = response.total
        }
      })

      // 加载候选人数量
      getCandidateList({ current: 1, size: 1, status: 1 }).then(response => {
        if (response.total) {
          this.stats.candidateCount = response.total
        }
      })

      // 加载推荐统计数据
      getRecommendationStatistics().then(response => {
        if (response.data) {
          this.stats.recommendCount = response.data.total || 0
          this.todoList[0].count = response.data.screening || 0
          this.todoList[1].count = response.data.interviewing || 0
          this.todoList[2].count = response.data.offer || 0
        }
      })
    },
    loadFunnelData() {
      getRecommendationStatistics().then(response => {
        if (response.data) {
          const total = response.data.total || 0
          if (total > 0) {
            this.funnelData[0].value = total
            this.funnelData[1].value = response.data.interviewing || 0
            this.funnelData[1].percentage = Math.round((response.data.interviewing || 0) / total * 100)
            this.funnelData[2].value = response.data.offer || 0
            this.funnelData[2].percentage = Math.round((response.data.offer || 0) / total * 100)
            this.funnelData[3].value = response.data.onboarded || 0
            this.funnelData[3].percentage = Math.round((response.data.onboarded || 0) / total * 100)
          }
        }
      })
    },
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },
    goTo(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
}

.card-panel {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  }

  .card-panel-icon-wrapper {
    float: left;
    width: 60px;
    height: 60px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;

    &.icon-client {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    &.icon-job {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }
    &.icon-candidate {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }
    &.icon-recommend {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }

    .card-panel-icon {
      font-size: 30px;
      color: #fff;
    }
  }

  .card-panel-description {
    float: right;
    text-align: right;

    .card-panel-text {
      line-height: 18px;
      color: rgba(0, 0, 0, 0.45);
      font-size: 14px;
      margin-bottom: 12px;
    }

    .card-panel-num {
      font-size: 30px;
      font-weight: bold;
      color: #303133;
    }
  }
}

.todo-list {
  padding: 10px;

  .todo-item {
    display: flex;
    align-items: center;
    padding: 15px;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .todo-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;
      color: #fff;
    }

    .todo-content {
      flex: 1;

      .todo-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
      }

      .todo-desc {
        font-size: 12px;
        color: #909399;
        margin-top: 5px;
      }
    }

    .todo-count {
      font-size: 20px;
      font-weight: bold;
      color: #409EFF;
    }
  }
}

.interview-list {
  padding: 10px;

  .interview-item {
    display: flex;
    align-items: center;
    padding: 15px;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .interview-time {
      width: 80px;
      font-size: 14px;
      font-weight: 500;
      color: #409EFF;
    }

    .interview-info {
      flex: 1;

      .interview-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
      }

      .interview-type {
        font-size: 12px;
        color: #909399;
        margin-top: 5px;
      }
    }
  }
}

.quick-actions {
  padding: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;

  .el-button {
    width: 45%;
  }
}

.funnel-chart {
  padding: 20px;

  .funnel-item {
    display: flex;
    align-items: center;
    margin-bottom: 15px;

    .funnel-label {
      width: 60px;
      color: #606266;
      font-size: 12px;
    }

    .funnel-bar {
      height: 24px;
      line-height: 24px;
      color: #fff;
      border-radius: 4px;
      text-align: center;
      font-size: 12px;
      transition: width 0.3s;
    }
  }
}
</style>
