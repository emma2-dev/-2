<template>
  <div class="my-articles">
    <div class="header">
      <div class="container">
        <h1 class="logo" @click="$router.push('/home')">个人博客</h1>
        <div class="nav">
          <router-link to="/home" class="nav-item">首页</router-link>
          <router-link to="/notes" class="nav-item">随笔</router-link>
          <router-link to="/message" class="nav-item">留言</router-link>
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
      <div class="article-list-wrapper">
        <h2>我的博客</h2>
        
        <el-table :data="articles" border style="margin-top: 20px">
          <el-table-column prop="title" label="标题" min-width="200"></el-table-column>
          <el-table-column prop="categoryName" label="分类" width="120"></el-table-column>
          <el-table-column prop="viewCount" label="浏览" width="80"></el-table-column>
          <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0" type="info">草稿</el-tag>
              <el-tag v-else-if="scope.row.status === 1" type="warning">待审核</el-tag>
              <el-tag v-else-if="scope.row.status === 2" type="success">已发布</el-tag>
              <el-tag v-else-if="scope.row.status === 3" type="danger">未通过</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="publishTime" label="发布时间" width="180"></el-table-column>
          <el-table-column label="操作" width="250">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="viewArticle(scope.row.id)">查看</el-button>
              <el-button size="mini" @click="editArticle(scope.row.id)">编辑</el-button>
              <el-button size="mini" type="danger" @click="deleteArticle(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          @current-change="handlePageChange"
          :current-page="page"
          :page-size="limit"
          :total="total"
          layout="total, prev, pager, next"
          style="margin-top: 20px">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MyArticles',
  data() {
    return {
      articles: [],
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
    this.loadArticles()
  },
  methods: {
    loadArticles() {
      const params = { page: this.page, limit: this.limit }
      this.$axios.get('/article/my-list', { params }).then(res => {
        this.articles = res.data.records
        this.total = res.data.total
      })
    },
    viewArticle(id) {
      window.open(`/#/article/${id}`, '_blank')
    },
    editArticle(id) {
      this.$router.push(`/article-edit/${id}`)
    },
    deleteArticle(id) {
      this.$confirm('确定删除该文章吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/article/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadArticles()
        })
      })
    },
    handlePageChange(page) {
      this.page = page
      this.loadArticles()
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
  max-width: 1200px;
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
  align-items: center;
}

.nav-item {
  color: #333;
  text-decoration: none;
  cursor: pointer;
}

.nav-item:hover {
  color: #435595;
}

.main-content {
  margin-top: 30px;
}

.article-list-wrapper {
  background: white;
  padding: 40px;
  border-radius: 8px;
}

h2 {
  margin: 0 0 20px;
}
</style>

