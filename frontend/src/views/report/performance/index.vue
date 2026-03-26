<template>
  <div class="performance-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-icon" style="background: #409EFF">
            <i class="el-icon-document"></i>
          </div>
          <div class="stats-content">
            <div class="stats-label">总推荐数</div>
            <div class="stats-value">{{ statistics.total || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-icon" style="background: #E6A23C">
            <i class="el-icon-search"></i>
          </div>
          <div class="stats-content">
            <div class="stats-label">简历筛选中</div>
            <div class="stats-value">{{ statistics.screening || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-icon" style="background: #67C23A">
            <i class="el-icon-user"></i>
          </div>
          <div class="stats-content">
            <div class="stats-label">面试中</div>
            <div class="stats-value">{{ statistics.interviewing || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-icon" style="background: #F56C6C">
            <i class="el-icon-circle-check"></i>
          </div>
          <div class="stats-content">
            <div class="stats-label">已入职</div>
            <div class="stats-value">{{ statistics.onboarded || 0 }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 转化率分析 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>推荐转化漏斗</span>
          </div>
          <div class="funnel-chart">
            <div class="funnel-item" v-for="(item, index) in funnelData" :key="index">
              <div class="funnel-label">{{ item.label }}</div>
              <div class="funnel-bar" :style="{ width: item.percentage + '%', background: item.color }">
                {{ item.value }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>状态分布</span>
          </div>
          <div class="pie-chart">
            <div class="pie-legend">
              <div class="legend-item" v-for="(item, index) in statusData" :key="index">
                <span class="legend-color" :style="{ background: item.color }"></span>
                <span class="legend-label">{{ item.label }}</span>
                <span class="legend-value">{{ item.value }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近推荐记录 -->
    <el-row :gutter="20" class="table-row">
      <el-col :span="24">
        <el-card class="chart-card">
          <div slot="header">
            <span>最近推荐记录</span>
          </div>
          <el-table :data="recommendationList" border stripe style="width: 100%">
            <el-table-column prop="jobTitle" label="职位" min-width="150"></el-table-column>
            <el-table-column prop="candidateName" label="候选人" width="100"></el-table-column>
            <el-table-column label="状态" width="120">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="推荐时间" width="160"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getRecommendationStatistics, getRecommendationList } from '@/api/recommendation'

export default {
  name: 'Performance',
  data() {
    return {
      statistics: {},
      recommendationList: [],
      funnelData: [
        { label: '总推荐', value: 0, percentage: 100, color: '#409EFF' },
        { label: '简历筛选', value: 0, percentage: 0, color: '#E6A23C' },
        { label: '面试中', value: 0, percentage: 0, color: '#67C23A' },
        { label: 'Offer', value: 0, percentage: 0, color: '#F56C6C' },
        { label: '入职', value: 0, percentage: 0, color: '#909399' }
      ],
      statusData: [
        { label: '待推荐', value: 0, color: '#909399' },
        { label: '简历筛选中', value: 0, color: '#E6A23C' },
        { label: '面试中', value: 0, color: '#409EFF' },
        { label: 'Offer谈判中', value: 0, color: '#F56C6C' },
        { label: '入职中', value: 0, color: '#67C23A' },
        { label: '已入职', value: 0, color: '#67C23A' }
      ]
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadRecommendations()
  },
  methods: {
    loadStatistics() {
      getRecommendationStatistics().then(response => {
        if (response.data) {
          this.statistics = response.data
          this.updateChartData()
        }
      })
    },
    loadRecommendations() {
      getRecommendationList({ current: 1, size: 10 }).then(response => {
        if (response.data) {
          this.recommendationList = response.data || []
        }
      })
    },
    updateChartData() {
      const total = this.statistics.total || 0
      if (total > 0) {
        this.funnelData[0].value = total
        this.funnelData[1].value = this.statistics.screening || 0
        this.funnelData[1].percentage = Math.round((this.statistics.screening || 0) / total * 100)
        this.funnelData[2].value = this.statistics.interviewing || 0
        this.funnelData[2].percentage = Math.round((this.statistics.interviewing || 0) / total * 100)
        this.funnelData[3].value = this.statistics.offer || 0
        this.funnelData[3].percentage = Math.round((this.statistics.offer || 0) / total * 100)
        this.funnelData[4].value = this.statistics.onboarded || 0
        this.funnelData[4].percentage = Math.round((this.statistics.onboarded || 0) / total * 100)
      }

      this.statusData[0].value = this.statistics.total - (this.statistics.screening || 0) - (this.statistics.interviewing || 0) - (this.statistics.offer || 0) - (this.statistics.onboarded || 0)
      this.statusData[1].value = this.statistics.screening || 0
      this.statusData[2].value = this.statistics.interviewing || 0
      this.statusData[4].value = this.statistics.offer || 0
      this.statusData[5].value = this.statistics.onboarded || 0
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
.performance-container {
  .stats-row {
    margin-bottom: 20px;
  }

  .stats-card {
    display: flex;
    align-items: center;
    padding: 20px;

    .stats-icon {
      width: 60px;
      height: 60px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 28px;
      color: #fff;
      margin-right: 15px;
    }

    .stats-content {
      flex: 1;

      .stats-label {
        color: #909399;
        font-size: 14px;
        margin-bottom: 5px;
      }

      .stats-value {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
      }
    }
  }

  .chart-card {
    margin-bottom: 20px;
  }

  .funnel-chart {
    padding: 20px;

    .funnel-item {
      display: flex;
      align-items: center;
      margin-bottom: 15px;

      .funnel-label {
        width: 80px;
        color: #606266;
      }

      .funnel-bar {
        height: 30px;
        line-height: 30px;
        color: #fff;
        border-radius: 4px;
        text-align: center;
        transition: width 0.3s;
      }
    }
  }

  .pie-chart {
    padding: 20px;

    .pie-legend {
      .legend-item {
        display: flex;
        align-items: center;
        margin-bottom: 10px;

        .legend-color {
          width: 16px;
          height: 16px;
          border-radius: 4px;
          margin-right: 10px;
        }

        .legend-label {
          flex: 1;
          color: #606266;
        }

        .legend-value {
          font-weight: bold;
        }
      }
    }
  }

  .table-row {
    margin-top: 20px;
  }
}
</style>
