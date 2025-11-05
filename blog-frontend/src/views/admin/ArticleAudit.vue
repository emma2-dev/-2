<template>
  <div class="article-audit">
    <h2>文章审核</h2>
    
    <el-table :data="articles" border style="margin-top: 20px">
      <el-table-column prop="id" label="ID" width="60"></el-table-column>
      <el-table-column prop="title" label="标题" min-width="200"></el-table-column>
      <el-table-column prop="authorName" label="作者" width="120"></el-table-column>
      <el-table-column prop="categoryName" label="分类" width="100"></el-table-column>
      <el-table-column prop="viewCount" label="浏览" width="80"></el-table-column>
      <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="提交时间" width="160">
        <template slot-scope="scope">
          {{ formatTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="viewArticle(scope.row.id)">查看</el-button>
          <el-button size="mini" type="success" @click="auditArticle(scope.row.id, 2)">通过</el-button>
          <el-button size="mini" type="danger" @click="auditArticle(scope.row.id, 3)">拒绝</el-button>
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
  name: 'ArticleAudit',
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
      const params = { page: this.page, limit: this.limit, status: 1 }
      this.$axios.get('/article/list', { params }).then(res => {
        this.articles = res.data.records
        this.total = res.data.total
      })
    },
    viewArticle(id) {
      // 在新窗口打开文章详情
      window.open(`/#/article/${id}`, '_blank')
    },
    auditArticle(id, status) {
      const action = status === 2 ? '通过' : '拒绝'
      this.$confirm(`确定${action}该文章吗？`, '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.put(`/article/${id}/audit`, null, { params: { status } }).then(() => {
          this.$message.success(`审核${action}成功`)
          this.loadArticles()
        })
      })
    },
    getStatusType(status) {
      const types = {
        0: 'info',
        1: 'warning',
        2: 'success',
        3: 'danger'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        0: '草稿',
        1: '待审核',
        2: '已发布',
        3: '未通过'
      }
      return texts[status] || '未知'
    },
    handlePageChange(page) {
      this.page = page
      this.loadArticles()
    },
    formatTime(time) {
      if (!time) return ''
      return new Date(time).toLocaleString()
    }
  }
}
</script>

<style scoped>
.article-audit {
  background: white;
  padding: 20px;
  border-radius: 4px;
}
</style>






