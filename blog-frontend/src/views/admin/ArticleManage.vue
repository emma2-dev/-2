<template>
  <div class="article-manage">
    <div class="header">
      <h2>文章管理</h2>
      <el-button type="primary" @click="$router.push('/admin/article-edit')">
        <i class="el-icon-plus"></i> 新建文章
      </el-button>
    </div>

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
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
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
</template>

<script>
export default {
  name: 'ArticleManage',
  data() {
    return {
      articles: [],
      page: 1,
      limit: 10,
      total: 0
    }
  },
  created() {
    this.loadArticles()
  },
  methods: {
    loadArticles() {
      const params = { page: this.page, limit: this.limit }
      this.$axios.get('/article/list', { params }).then(res => {
        this.articles = res.data.records
        this.total = res.data.total
      })
    },
    editArticle(id) {
      this.$router.push(`/admin/article-edit/${id}`)
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
    }
  }
}
</script>

<style scoped>
.article-manage {
  background: white;
  padding: 20px;
  border-radius: 4px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h2 {
  margin: 0;
}
</style>


