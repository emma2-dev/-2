<template>
  <div class="message-manage">
    <h2>留言管理</h2>

    <el-table :data="messages" border style="margin-top: 20px">
      <el-table-column prop="content" label="内容" min-width="200"></el-table-column>
      <el-table-column prop="userName" label="用户" width="120"></el-table-column>
      <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
      <el-table-column prop="createTime" label="留言时间" width="180"></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="deleteMessage(scope.row.id)">删除</el-button>
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
  name: 'MessageManage',
  data() {
    return {
      messages: [],
      page: 1,
      limit: 10,
      total: 0
    }
  },
  created() {
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
    deleteMessage(id) {
      this.$confirm('确定删除该留言吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/message/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadMessages()
        })
      })
    },
    handlePageChange(page) {
      this.page = page
      this.loadMessages()
    }
  }
}
</script>

<style scoped>
.message-manage {
  background: white;
  padding: 20px;
  border-radius: 4px;
}
</style>


