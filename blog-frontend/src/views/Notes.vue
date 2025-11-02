<template>
  <div class="notes">
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
      <div v-if="isLogin" class="note-form">
        <el-input 
          type="textarea" 
          v-model="noteContent" 
          placeholder="分享你的想法..."
          :rows="4">
        </el-input>
        <el-button type="primary" @click="submitNote" style="margin-top: 10px">发布随笔</el-button>
      </div>

      <div class="note-list">
        <div v-for="note in notes" :key="note.id" class="note-item">
          <div class="note-header">
            <span class="author">{{ note.authorName }}</span>
            <span class="time">{{ formatTime(note.createTime) }}</span>
          </div>
          <div class="note-content">{{ note.content }}</div>
          <div class="note-actions">
            <span @click="likeNote(note.id)">
              <i class="el-icon-star-off"></i> {{ note.likeCount }}
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
  name: 'Notes',
  data() {
    return {
      notes: [],
      noteContent: '',
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
    this.loadNotes()
  },
  methods: {
    loadNotes() {
      const params = { page: this.page, limit: this.limit }
      this.$axios.get('/note/list', { params }).then(res => {
        this.notes = res.data.records
        this.total = res.data.total
      })
    },
    submitNote() {
      if (!this.noteContent.trim()) {
        this.$message.warning('请输入随笔内容')
        return
      }
      this.$axios.post('/note', { content: this.noteContent }).then(() => {
        this.$message.success('发布成功')
        this.noteContent = ''
        this.loadNotes()
      })
    },
    likeNote(id) {
      this.$axios.post(`/note/${id}/like`).then(() => {
        this.$message.success('点赞成功')
        this.loadNotes()
      })
    },
    handlePageChange(page) {
      this.page = page
      this.loadNotes()
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

.note-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.note-list {
  min-height: 500px;
}

.note-item {
  background: white;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
}

.note-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.author {
  font-weight: bold;
}

.time {
  color: #999;
  font-size: 14px;
}

.note-content {
  line-height: 1.8;
  margin: 15px 0;
}

.note-actions {
  color: #999;
  font-size: 14px;
}

.note-actions span {
  cursor: pointer;
  margin-right: 20px;
}

.note-actions span:hover {
  color: #435595;
}
</style>


