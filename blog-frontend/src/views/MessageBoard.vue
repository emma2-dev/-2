<template>
  <div class="message-board">
    <div class="header">
      <div class="container">
        <h1 class="logo" @click="$router.push('/home')">个人博客</h1>
        <div class="nav">
          <router-link to="/home" class="nav-item">首页</router-link>
          <router-link to="/notes" class="nav-item">随笔</router-link>
          <router-link to="/message" class="nav-item">留言</router-link>
          <router-link v-if="!isLogin" to="/login" class="nav-item">登录</router-link>
          <router-link v-if="isLogin" to="/user" class="nav-item">个人中心</router-link>
          <router-link v-if="isLogin" to="/my-articles" class="nav-item">我的博客</router-link>
          <router-link v-if="isAdmin" to="/admin/dashboard" class="nav-item">后台管理</router-link>
          <router-link v-if="isLogin" to="/article-edit" class="nav-item publish-btn">
            <i class="el-icon-edit"></i> 发布文章
          </router-link>
          <a v-if="isLogin" @click="handleLogout" class="nav-item">退出</a>
        </div>
      </div>
    </div>

    <div class="container main-content">
      <div class="message-board-title">
        <h2>留言板</h2>
        <p>欢迎留下您的足迹</p>
      </div>

      <div v-if="isLogin" class="message-form">
        <el-input 
          type="textarea" 
          v-model="messageContent" 
          placeholder="写下你的留言..."
          :rows="4">
        </el-input>
        <el-button type="primary" @click="submitMessage" style="margin-top: 10px">发表留言</el-button>
      </div>
      <div v-else class="login-tip">
        <router-link to="/login">登录</router-link> 后才能留言
      </div>

      <div class="message-list">
        <div v-for="message in messages" :key="message.id" class="message-item">
          <div class="message-header">
            <span class="user">{{ message.userName }}</span>
            <span class="time">{{ formatTime(message.createTime) }}</span>
          </div>
          <div class="message-content">{{ message.content }}</div>
          <div class="message-actions">
            <span @click="likeMessage(message.id)">
              <i class="el-icon-star-off"></i> {{ message.likeCount }}
            </span>
          </div>
        </div>
      </div>

      <el-pagination
        @current-change="handlePageChange"
        :current-page="page"
        :page-size="limit"
        :total="total"
        layout="prev, pager, next">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MessageBoard',
  data() {
    return {
      messages: [],
      messageContent: '',
      page: 1,
      limit: 10,
      total: 0
    }
  },
  computed: {
    isLogin() {
      return !!this.$store.state.token
    },
    isAdmin() {
      return this.$store.state.user && this.$store.state.user.role === 'ADMIN'
    }
  },
  created() {
    if (this.isLogin) {
      this.$store.dispatch('getUserInfo')
    }
    this.loadMessages()
  },
  methods: {
    loadMessages() {
      const params = { page: this.page, limit: this.limit }
      this.$axios.get('/message/list', { params }).then(res => {
        this.messages = res.data.records
        this.total = res.data.total
      })
    },
    submitMessage() {
      if (!this.messageContent.trim()) {
        this.$message.warning('请输入留言内容')
        return
      }
      const formData = new FormData()
      formData.append('content', this.messageContent)
      this.$axios.post('/message', formData).then(() => {
        this.$message.success('留言成功')
        this.messageContent = ''
        this.loadMessages()
      })
    },
    likeMessage(id) {
      this.$axios.post(`/message/${id}/like`).then(() => {
        this.$message.success('点赞成功')
        this.loadMessages()
      })
    },
    handlePageChange(page) {
      this.page = page
      this.loadMessages()
    },
    formatTime(time) {
      if (!time) return ''
      return new Date(time).toLocaleString()
    },
    handleLogout() {
      this.$store.dispatch('logout')
      this.$message.success('退出成功')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.header {
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  padding: 20px 0;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.header .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 28px;
  font-weight: bold;
  color: #435595;
  margin: 0;
  cursor: pointer;
}

.nav {
  display: flex;
  gap: 20px;
}

.nav-item {
  color: #333;
  text-decoration: none;
  cursor: pointer;
}

.nav-item:hover {
  color: #435595;
}

.publish-btn {
  background: #435595 !important;
  color: white !important;
  padding: 8px 16px !important;
  border-radius: 4px;
  transition: all 0.3s;
}

.publish-btn:hover {
  background: #3F4486 !important;
  color: white !important;
}

.main-content {
  margin-top: 30px;
}

.message-board-title {
  text-align: center;
  margin-bottom: 30px;
}

.message-board-title h2 {
  font-size: 32px;
  color: #333;
  margin-bottom: 10px;
}

.message-board-title p {
  color: #999;
}

.message-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.login-tip {
  text-align: center;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 4px;
  margin-bottom: 20px;
}

.message-list {
  min-height: 500px;
}

.message-item {
  background: white;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.user {
  font-weight: bold;
}

.time {
  color: #999;
  font-size: 14px;
}

.message-content {
  line-height: 1.8;
  margin: 15px 0;
}

.message-actions {
  color: #999;
  font-size: 14px;
}

.message-actions span {
  cursor: pointer;
}

.message-actions span:hover {
  color: #435595;
}
</style>


