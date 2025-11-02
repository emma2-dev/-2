<template>
  <div class="article-detail">
    <div class="header">
      <div class="container">
        <h1 class="logo" @click="$router.push('/home')">个人博客</h1>
      </div>
    </div>

    <div class="container main-content">
      <div class="article-container">
        <h1 class="title">{{ article.title }}</h1>
        <div class="meta">
          <span><i class="el-icon-user"></i> {{ article.authorName }}</span>
          <span><i class="el-icon-folder"></i> {{ article.categoryName }}</span>
          <span><i class="el-icon-view"></i> {{ article.viewCount }}</span>
          <span><i class="el-icon-time"></i> {{ formatTime(article.publishTime) }}</span>
        </div>
        <div class="tags" v-if="article.tags && article.tags.length">
          <el-tag v-for="tag in article.tags" :key="tag.id" size="small" :color="tag.color">
            {{ tag.name }}
          </el-tag>
        </div>
        <div class="content markdown-body" v-html="renderedContent"></div>
        <div class="actions">
          <el-button type="primary" icon="el-icon-star-off" @click="handleLike">
            点赞 ({{ article.likeCount }})
          </el-button>
        </div>

        <div class="comments">
          <h3>评论 ({{ comments.length }})</h3>
          <div v-if="isLogin" class="comment-form">
            <el-input 
              type="textarea" 
              v-model="commentContent" 
              placeholder="写下你的评论..."
              :rows="3">
            </el-input>
            <el-button type="primary" @click="submitComment" style="margin-top: 10px">发表评论</el-button>
          </div>
          <div v-else class="login-tip">
            <router-link to="/login">登录</router-link> 后才能评论
          </div>

          <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-user">{{ comment.userName }}</div>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-time">{{ formatTime(comment.createTime) }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ArticleDetail',
  data() {
    return {
      article: {},
      comments: [],
      commentContent: '',
      renderedContent: ''
    }
  },
  computed: {
    isLogin() {
      return !!this.$store.state.token
    }
  },
  created() {
    this.loadArticle()
    this.loadComments()
  },
  methods: {
    loadArticle() {
      const id = this.$route.params.id
      this.$axios.get(`/article/${id}`).then(res => {
        this.article = res.data
        // 将 Markdown 渲染为 HTML
        this.renderContent()
      })
    },
    async renderContent() {
      if (!this.article.content) {
        this.renderedContent = ''
        return
      }
      // 使用 markdown-it 渲染器
      if (this.$markdownit) {
        let content = this.article.content
        // 先处理纯数字的图片URL（文件ID）
        content = await this.processNumericImageUrls(content)
        // 然后处理其他图片URL
        content = this.processImageUrls(content)
        this.renderedContent = this.$markdownit.render(content)
      } else {
        // 如果 markdown-it 不可用，使用简单替换
        this.renderedContent = this.article.content
      }
    },
    async processNumericImageUrls(content) {
      if (!content) return content
      
      // 查找所有纯数字的图片URL
      const matches = content.matchAll(/!\[([^\]]*)\]\((\d+)\)/g)
      const fileIdMap = {}
      
      // 收集所有文件ID
      for (const match of matches) {
        const fileId = match[2]
        if (!fileIdMap[fileId]) {
          fileIdMap[fileId] = true
        }
      }
      
      // 批量获取文件信息
      const promises = Object.keys(fileIdMap).map(fileId => 
        this.$axios.get(`/file/image/${fileId}`).then(res => {
          if (res.data && res.data.fileName) {
            fileIdMap[fileId] = res.data.fileName
          }
        }).catch(() => {
          // 忽略错误，保持原值
        })
      )
      
      await Promise.all(promises)
      
      // 替换Markdown中的图片URL
      let processedContent = content
      Object.keys(fileIdMap).forEach(fileId => {
        if (typeof fileIdMap[fileId] === 'string') {
          const fileName = fileIdMap[fileId]
          processedContent = processedContent.replace(
            new RegExp(`!\\[([^\\]]*)\\]\\(${fileId}\\)`, 'g'),
            `![\$1](/files/${fileName})`
          )
        }
      })
      
      return processedContent
    },
    processImageUrls(content) {
      if (!content) return content
      // 处理Markdown图片语法中的URL
      return content.replace(/!\[([^\]]*)\]\(([^)]+)\)/g, (match, alt, url) => {
        const processedUrl = this.getImageUrl(url)
        return `![${alt}](${processedUrl})`
      })
    },
    getImageUrl(url) {
      if (!url) return url
      // 如果是完整URL，直接返回
      if (url.startsWith('http://') || url.startsWith('https://')) {
        return url
      }
      // 如果是相对路径，拼接文件访问地址
      if (url.startsWith('/files/')) {
        return `/api${url}`
      }
      // 如果是文件路径（如 C:\...），需要通过文件服务访问
      if (url.indexOf('\\') >= 0) {
        const fileName = url.split(/[/\\]/).pop()
        return `/api/files/${fileName}`
      }
      // 如果是纯数字，可能是文件ID，需要通过ID获取文件名
      if (/^\d+$/.test(url)) {
        return `/api/file/image/${url}`
      }
      // 如果只是文件名（包含.jpg等扩展名），通过文件服务访问
      if (url.match(/\.(jpg|jpeg|png|gif|bmp|webp)$/i)) {
        // 提取纯文件名
        const fileName = url.split(/[/\\]/).pop()
        return `/api/files/${fileName}`
      }
      return url
    },
    loadComments() {
      const id = this.$route.params.id
      this.$axios.get('/comment/target', {
        params: { type: 1, targetId: id }
      }).then(res => {
        this.comments = res.data
      })
    },
    handleLike() {
      const id = this.$route.params.id
      this.$axios.post(`/article/${id}/like`).then(() => {
        this.$message.success('点赞成功')
        this.loadArticle()
      })
    },
    submitComment() {
      if (!this.commentContent.trim()) {
        this.$message.warning('请输入评论内容')
        return
      }
      const data = {
        type: 1,
        targetId: this.$route.params.id,
        content: this.commentContent
      }
      this.$axios.post('/comment', data).then(() => {
        this.$message.success('评论成功')
        this.commentContent = ''
        this.loadComments()
      })
    },
    formatTime(time) {
      if (!time) return ''
      return new Date(time).toLocaleString()
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
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.logo {
  font-size: 28px;
  font-weight: bold;
  color: #435595;
  margin: 0;
  cursor: pointer;
}

.main-content {
  margin-top: 30px;
}

.article-container {
  background: white;
  padding: 40px;
  border-radius: 8px;
}

.title {
  font-size: 32px;
  margin: 0 0 20px;
}

.meta {
  display: flex;
  gap: 20px;
  color: #999;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.tags {
  margin-bottom: 20px;
}

.tags .el-tag {
  margin-right: 10px;
}

.content {
  line-height: 2;
  font-size: 16px;
  color: #333;
  margin: 30px 0;
}

/* Markdown样式 */
.markdown-body {
  line-height: 1.8;
}

.markdown-body h1,
.markdown-body h2,
.markdown-body h3,
.markdown-body h4,
.markdown-body h5,
.markdown-body h6 {
  margin: 20px 0 10px;
  font-weight: bold;
  line-height: 1.5;
}

.markdown-body h1 {
  font-size: 2em;
  border-bottom: 2px solid #eee;
  padding-bottom: 10px;
}

.markdown-body h2 {
  font-size: 1.5em;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
}

.markdown-body h3 {
  font-size: 1.3em;
}

.markdown-body h4 {
  font-size: 1.1em;
}

.markdown-body p {
  margin: 10px 0;
}

.markdown-body ul,
.markdown-body ol {
  margin: 10px 0;
  padding-left: 30px;
}

.markdown-body li {
  margin: 5px 0;
}

.markdown-body blockquote {
  border-left: 4px solid #ddd;
  padding-left: 15px;
  margin: 15px 0;
  color: #666;
}

.markdown-body code {
  background-color: #f5f5f5;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: 'Courier New', monospace;
  font-size: 0.9em;
}

.markdown-body pre {
  background-color: #f5f5f5;
  padding: 15px;
  border-radius: 5px;
  overflow-x: auto;
}

.markdown-body pre code {
  background-color: transparent;
  padding: 0;
}

.markdown-body strong {
  font-weight: bold;
}

.markdown-body em {
  font-style: italic;
}

.markdown-body hr {
  border: none;
  border-top: 2px solid #eee;
  margin: 20px 0;
}

.markdown-body img {
  max-width: 100%;
  height: auto;
  margin: 15px 0;
  border-radius: 5px;
}

.markdown-body table {
  border-collapse: collapse;
  width: 100%;
  margin: 15px 0;
}

.markdown-body table th,
.markdown-body table td {
  border: 1px solid #ddd;
  padding: 8px 12px;
  text-align: left;
}

.markdown-body table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.actions {
  text-align: center;
  margin: 40px 0;
}

.comments {
  margin-top: 50px;
}

.comments h3 {
  margin-bottom: 20px;
}

.comment-form {
  margin-bottom: 30px;
}

.login-tip {
  text-align: center;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 4px;
  margin-bottom: 20px;
}

.comment-list {
  margin-top: 20px;
}

.comment-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.comment-user {
  font-weight: bold;
  margin-bottom: 5px;
}

.comment-content {
  margin: 10px 0;
}

.comment-time {
  font-size: 12px;
  color: #999;
}
</style>


