<template>
  <div class="note-manage">
    <h2>随笔管理</h2>

    <el-table :data="notes" border style="margin-top: 20px">
      <el-table-column prop="content" label="内容" min-width="200"></el-table-column>
      <el-table-column prop="authorName" label="作者" width="120"></el-table-column>
      <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180"></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="deleteNote(scope.row.id)">删除</el-button>
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
  name: 'NoteManage',
  data() {
    return {
      notes: [],
      page: 1,
      limit: 10,
      total: 0
    }
  },
  created() {
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
    deleteNote(id) {
      this.$confirm('确定删除该随笔吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/note/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadNotes()
        })
      })
    },
    handlePageChange(page) {
      this.page = page
      this.loadNotes()
    }
  }
}
</script>

<style scoped>
.note-manage {
  background: white;
  padding: 20px;
  border-radius: 4px;
}
</style>


