<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="200px">
        <div class="logo">博客管理系统</div>
        <el-menu 
          :default-active="$route.path" 
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff">
          <el-menu-item index="/admin/dashboard">
            <i class="el-icon-s-home"></i>
            <span>控制台</span>
          </el-menu-item>
          <el-submenu index="content">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>内容管理</span>
            </template>
            <el-menu-item index="/admin/articles">文章管理</el-menu-item>
            <el-menu-item index="/admin/article-audit">文章审核</el-menu-item>
            <el-menu-item index="/admin/notes">随笔管理</el-menu-item>
          </el-submenu>
          <el-submenu index="system">
            <template slot="title">
              <i class="el-icon-setting"></i>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/admin/categories">分类管理</el-menu-item>
            <el-menu-item index="/admin/tags">标签管理</el-menu-item>
            <el-menu-item index="/admin/messages">留言管理</el-menu-item>
            <el-menu-item index="/admin/comments">评论管理</el-menu-item>
            <el-menu-item index="/admin/files">文件管理</el-menu-item>
            <el-menu-item index="/admin/users">用户管理</el-menu-item>
            <el-menu-item index="/admin/carousels">轮播图管理</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="header-right">
            <span class="username">{{ username }}</span>
            <el-button type="text" @click="goHome">返回首页</el-button>
            <el-button type="text" @click="logout">退出</el-button>
          </div>
        </el-header>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'AdminLayout',
  computed: {
    username() {
      return this.$store.state.user ? this.$store.state.user.nickname : ''
    }
  },
  created() {
    this.$store.dispatch('getUserInfo')
  },
  methods: {
    goHome() {
      this.$router.push('/home')
    },
    logout() {
      this.$store.dispatch('logout')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
}

.el-aside {
  background-color: #304156;
  height: 100vh;
  overflow-y: auto;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: white;
  font-size: 18px;
  font-weight: bold;
  background-color: #2b3a4b;
}

.el-header {
  background-color: white;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.username {
  color: #333;
}

.el-main {
  background-color: #f0f2f5;
  min-height: calc(100vh - 60px);
}
</style>


