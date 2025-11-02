<template>
  <div class="comment-manage">
    <h2>评论管理</h2>

    <el-table :data="comments" border style="margin-top: 20px">
      <el-table-column prop="content" label="内容" min-width="200"></el-table-column>
      <el-table-column prop="userName" label="用户" width="120"></el-table-column>
      <el-table-column prop="type" label="类型" width="100">
        <template slot-scope="scope">
          {{ scope.row.type === 1 ? '文章评论' : '随笔评论' }}
        </template>
      </el-table-column>
      <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
      <el-table-column prop="createTime" label="评论时间" width="180"></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="deleteComment(scope.row.id)">删除</el-button>
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
  name: 'CommentManage',
  data() {
    return {
      comments: [],
      page: 1,
      limit: 10,
      total: 0
    }
  },
  created() {
    this.loadComments()
  },
  methods: {
    loadComments() {
      const params = { page: this.page, limit: this.limit }
      this.$axios.get('/comment/list', { params }).then(res => {
        this.comments = res.data.records
        this.total = res.data.total
      })
    },
    deleteComment(id) {
      this.$confirm('确定删除该评论吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/comment/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadComments()
        })
      })
    },
    handlePageChange(page) {
      this.page = page
      this.loadComments()
    }
  }
}
</script>

<style scoped>
.comment-manage {
  background: white;
  padding: 20px;
  border-radius: 4px;
}
</style>


