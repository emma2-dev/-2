<template>
  <div class="file-manage">
    <div class="header">
      <h2>文件管理</h2>
      <el-upload
        action="/api/file/upload"
        :headers="uploadHeaders"
        :on-success="handleUploadSuccess"
        :show-file-list="false">
        <el-button type="primary">
          <i class="el-icon-upload"></i> 上传文件
        </el-button>
      </el-upload>
    </div>

    <el-table :data="files" border style="margin-top: 20px">
      <el-table-column prop="originalName" label="文件名" min-width="200"></el-table-column>
      <el-table-column prop="fileSize" label="大小" width="120">
        <template slot-scope="scope">
          {{ formatFileSize(scope.row.fileSize) }}
        </template>
      </el-table-column>
      <el-table-column prop="category" label="类型" width="100"></el-table-column>
      <el-table-column prop="createTime" label="上传时间" width="180"></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="deleteFile(scope.row.id)">删除</el-button>
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
  name: 'FileManage',
  data() {
    return {
      files: [],
      page: 1,
      limit: 10,
      total: 0,
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    }
  },
  created() {
    this.loadFiles()
  },
  methods: {
    loadFiles() {
      const params = { page: this.page, limit: this.limit }
      this.$axios.get('/file/list', { params }).then(res => {
        this.files = res.data.records
        this.total = res.data.total
      })
    },
    handleUploadSuccess() {
      this.$message.success('上传成功')
      this.loadFiles()
    },
    deleteFile(id) {
      this.$confirm('确定删除该文件吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/file/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadFiles()
        })
      })
    },
    formatFileSize(size) {
      if (size < 1024) return size + ' B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB'
      return (size / 1024 / 1024).toFixed(2) + ' MB'
    },
    handlePageChange(page) {
      this.page = page
      this.loadFiles()
    }
  }
}
</script>

<style scoped>
.file-manage {
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


