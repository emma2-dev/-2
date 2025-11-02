<template>
  <div class="dashboard">
    <h2>控制台</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #409eff">
            <i class="el-icon-document"></i>
          </div>
          <div class="stat-info">
            <div class="stat-label">文章总数</div>
            <div class="stat-value">{{ stats.articleCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #67c23a">
            <i class="el-icon-chat-line-square"></i>
          </div>
          <div class="stat-info">
            <div class="stat-label">随笔总数</div>
            <div class="stat-value">{{ stats.noteCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #e6a23c">
            <i class="el-icon-message"></i>
          </div>
          <div class="stat-info">
            <div class="stat-label">留言总数</div>
            <div class="stat-value">{{ stats.messageCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #f56c6c">
            <i class="el-icon-user"></i>
          </div>
          <div class="stat-info">
            <div class="stat-label">用户总数</div>
            <div class="stat-value">{{ stats.userCount }}</div>
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
      stats: {
        articleCount: 0,
        noteCount: 0,
        messageCount: 0,
        userCount: 0
      }
    }
  },
  created() {
    this.loadStatistics()
  },
  methods: {
    loadStatistics() {
      this.$axios.get('/statistics').then(res => {
        this.stats = res.data || this.stats
      }).catch(() => {
        // 加载失败时保持默认值0
      })
    }
  }
}
</script>

<style scoped>
.dashboard h2 {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  margin-right: 20px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  color: #999;
  font-size: 14px;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}
</style>


