<template>
  <div class="home">
    <div class="header">
      <div class="container">
        <h1 class="logo">个人博客</h1>
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

    <div class="container">
      <!-- 轮播图区域 -->
      <div class="carousel-wrapper" v-if="carousels.length > 0">
        <el-carousel :interval="4000" type="card" height="300px">
          <el-carousel-item v-for="item in carousels" :key="item.id">
            <a :href="item.targetUrl || '#'" :target="item.targetUrl ? '_blank' : '_self'" class="carousel-link">
              <img :src="getImageUrl(item.imgUrl)" :alt="item.title" class="carousel-image">
              <div class="carousel-title" v-if="item.title">{{ item.title }}</div>
            </a>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>

    <div class="container main-content">
      <div class="content">
        <!-- 搜索框 -->
        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索文章..."
            clearable
            @keyup.enter.native="handleSearch"
            @clear="handleClearSearch">
            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
          </el-input>
        </div>

        <div class="category-filter">
          <el-tag 
            :type="currentCategory === null ? 'primary' : 'info'" 
            @click="filterByCategory(null)"
            style="cursor: pointer; margin-right: 10px">
            全部
          </el-tag>
          <el-tag 
            v-for="cat in categories" 
            :key="cat.id"
            :type="currentCategory === cat.id ? 'primary' : 'info'"
            @click="filterByCategory(cat.id)"
            style="cursor: pointer; margin-right: 10px">
            {{ cat.name }}
          </el-tag>
        </div>

        <div class="article-list">
          <div v-for="article in articles" :key="article.id" class="article-item" @click="goToDetail(article.id)">
            <div class="article-cover" v-if="article.coverImage">
              <img :src="getImageUrl(article.coverImage)" :alt="article.title">
            </div>
            <div class="article-info">
              <h3 class="article-title">
                <el-tag v-if="article.isTop === 1" type="danger" size="mini">置顶</el-tag>
                {{ article.title }}
              </h3>
              <p class="article-summary">{{ article.summary }}</p>
              <div class="article-meta">
                <span><i class="el-icon-user"></i> {{ article.authorName }}</span>
                <span><i class="el-icon-view"></i> {{ article.viewCount }}</span>
                <span><i class="el-icon-star-off"></i> {{ article.likeCount }}</span>
                <span><i class="el-icon-chat-dot-round"></i> {{ article.commentCount }}</span>
                <span><i class="el-icon-time"></i> {{ formatTime(article.publishTime) }}</span>
              </div>
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

      <div class="sidebar">
        <div class="widget">
          <h4>推荐文章</h4>
          <div v-for="item in recommendArticles" :key="item.id" class="recommend-item" @click="goToDetail(item.id)">
            {{ item.title }}
          </div>
        </div>
        
        <div class="widget">
          <h4>热门标签</h4>
          <div class="tag-list">
            <el-tag 
              v-for="tag in hotTags" 
              :key="tag.id" 
              :type="tag.color ? 'info' : 'primary'"
              size="small" 
              style="margin: 5px 5px 5px 0; cursor: pointer;"
              @click="searchByTag(tag.name)">
              {{ tag.name }}
            </el-tag>
          </div>
        </div>
        
        <div class="widget">
          <h4>热门分类</h4>
          <div class="category-list">
            <div 
              v-for="cat in hotCategories" 
              :key="cat.id" 
              class="category-item" 
              @click="filterByCategory(cat.id)">
              {{ cat.name }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      articles: [],
      categories: [],
      recommendArticles: [],
      carousels: [],
      hotTags: [],
      hotCategories: [],
      currentCategory: null,
      page: 1,
      limit: 10,
      total: 0,
      searchKeyword: ''
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
    this.loadCarousels()
    this.loadCategories()
    this.loadArticles()
    this.loadRecommendArticles()
    this.loadHotTags()
    this.loadHotCategories()
    if (this.isLogin) {
      this.$store.dispatch('getUserInfo')
    }
  },
  methods: {
    getImageUrl(url) {
      if (!url) return ''
      // 如果是完整URL，直接返回
      if (url.startsWith('http://') || url.startsWith('https://')) {
        return url
      }
      // 如果是相对路径，拼接文件访问地址
      if (url.startsWith('/files/')) {
        return `/api${url}`
      }
      // 如果是文件路径（如 C:\...或/filename），需要通过文件服务访问
      if (url.indexOf('\\') >= 0) {
        // Windows路径，提取文件名
        const fileName = url.split(/[/\\]/).pop()
        return `/api/files/${fileName}`
      }
      // 如果是文件名（不含路径分隔符），直接访问文件服务
      if (url.indexOf('/') === -1 && url.indexOf('\\') === -1) {
        return `/api/files/${url}`
      }
      // 其他情况，尝试通过文件服务访问
      return `/api/files/${url.split(/[/\\]/).pop()}`
    },
    loadCarousels() {
      this.$axios.get('/carousel/list').then(res => {
        this.carousels = res.data || []
      }).catch(() => {
        this.carousels = []
      })
    },
    loadCategories() {
      this.$axios.get('/category/list').then(res => {
        this.categories = res.data
      })
    },
    loadArticles() {
      const params = {
        page: this.page,
        limit: this.limit,
        status: 2,  // 只显示已发布的文章
        categoryId: this.currentCategory
      }
      // 如果有搜索关键词，添加到参数中
      if (this.searchKeyword && this.searchKeyword.trim()) {
        params.keyword = this.searchKeyword.trim()
      }
      this.$axios.get('/article/list', { params }).then(res => {
        this.articles = res.data.records
        this.total = res.data.total
      })
    },
    handleSearch() {
      this.page = 1
      this.loadArticles()
    },
    handleClearSearch() {
      this.searchKeyword = ''
      this.page = 1
      this.loadArticles()
    },
    loadRecommendArticles() {
      this.$axios.get('/article/recommend', { params: { limit: 5 } }).then(res => {
        this.recommendArticles = res.data
      })
    },
    loadHotTags() {
      this.$axios.get('/tag/hot', { params: { limit: 10 } }).then(res => {
        this.hotTags = res.data || []
      }).catch(() => {
        this.hotTags = []
      })
    },
    loadHotCategories() {
      this.$axios.get('/category/hot', { params: { limit: 5 } }).then(res => {
        this.hotCategories = res.data || []
      }).catch(() => {
        this.hotCategories = []
      })
    },
    filterByCategory(categoryId) {
      this.currentCategory = categoryId
      this.page = 1
      this.loadArticles()
    },
    searchByTag(tagName) {
      this.searchKeyword = tagName
      this.page = 1
      this.loadArticles()
    },
    handlePageChange(page) {
      this.page = page
      this.loadArticles()
    },
    goToDetail(id) {
      this.$router.push(`/article/${id}`)
    },
    formatTime(time) {
      if (!time) return ''
      return new Date(time).toLocaleDateString()
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
}

.nav {
  display: flex;
  gap: 20px;
}

.nav-item {
  color: #333;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.3s;
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

.carousel-wrapper {
  margin: 30px auto 30px;
  width: 100%;
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.carousel-link {
  display: block;
  position: relative;
  width: 100%;
  height: 100%;
  text-decoration: none;
}

.carousel-image {
  width: 100%;
  height: 300px;
  object-fit: cover;
  border-radius: 8px;
}

.carousel-title {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  color: white;
  padding: 20px;
  font-size: 18px;
  font-weight: bold;
  border-radius: 0 0 8px 8px;
}

.main-content {
  margin-top: 30px;
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 30px;
  align-items: start;
}

.search-box {
  margin-bottom: 20px;
}

.category-filter {
  margin-bottom: 20px;
}

.article-list {
  min-height: 500px;
}

.article-item {
  background: white;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  display: flex;
  gap: 20px;
}

.article-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.article-cover {
  width: 200px;
  height: 150px;
  flex-shrink: 0;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.article-info {
  flex: 1;
}

.article-title {
  font-size: 20px;
  margin: 0 0 10px;
  color: #333;
}

.article-summary {
  color: #666;
  margin: 10px 0;
  line-height: 1.6;
}

.article-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #999;
}

.sidebar {
  position: sticky;
  top: 20px;
}

.widget {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.widget h4 {
  margin: 0 0 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #435595;
}

.recommend-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  color: #333;
}

.recommend-item:hover {
  color: #435595;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
}

.category-item {
  padding: 8px 0;
  cursor: pointer;
  transition: color 0.3s;
  border-bottom: 1px solid #eee;
}

.category-item:hover {
  color: #435595;
}

.category-item:last-child {
  border-bottom: none;
}
</style>


