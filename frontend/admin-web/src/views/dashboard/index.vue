<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-user">
              <i class="el-icon-user card-panel-icon"></i>
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">用户总数</div>
              <div class="card-panel-num">1,234</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-role">
              <i class="el-icon-peoples card-panel-icon"></i>
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">角色数量</div>
              <div class="card-panel-num">8</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-menu">
              <i class="el-icon-menu card-panel-icon"></i>
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">菜单数量</div>
              <div class="card-panel-num">26</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-visitor">
              <i class="el-icon-view card-panel-icon"></i>
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">今日访问</div>
              <div class="card-panel-num">568</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header">
            <span>系统信息</span>
          </div>
          <div class="system-info">
            <p><strong>系统版本：</strong>1.0.0</p>
            <p><strong>前端框架：</strong>Vue 2.6 + Element UI 2.15</p>
            <p><strong>后端框架：</strong>Spring Cloud Hoxton</p>
            <p><strong>当前时间：</strong>{{ currentTime }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      currentTime: new Date().toLocaleString()
    }
  },
  mounted() {
    this.timer = setInterval(() => {
      this.currentTime = new Date().toLocaleString()
    }, 1000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
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
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}

.card-panel-icon-wrapper {
  float: left;
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;

  &.icon-user {
    background: #40c9c6;
  }
  &.icon-role {
    background: #36a3f7;
  }
  &.icon-menu {
    background: #f4516c;
  }
  &.icon-visitor {
    background: #34bfa3
  }
}

.card-panel-icon {
  font-size: 30px;
  color: #fff;
}

.card-panel-description {
  text-align: right;
  float: right;
}

.card-panel-text {
  line-height: 18px;
  color: rgba(0, 0, 0, 0.45);
  font-size: 14px;
  margin-bottom: 12px;
}

.card-panel-num {
  font-size: 30px;
  font-weight: bold;
}

.system-info {
  p {
    margin: 10px 0;
    font-size: 14px;
    color: #666;
  }
}
</style>
