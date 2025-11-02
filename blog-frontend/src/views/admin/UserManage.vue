<template>
  <div class="user-manage">
    <h2>用户管理</h2>

    <el-table :data="users" border style="margin-top: 20px">
      <el-table-column prop="username" label="用户名" width="150"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="150"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
      <el-table-column prop="role" label="角色" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.role === 'ADMIN'" type="danger">管理员</el-tag>
          <el-tag v-else type="success">普通用户</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success">正常</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="180"></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button 
            v-if="scope.row.role !== 'ADMIN'"
            size="mini" 
            type="danger" 
            @click="deleteUser(scope.row.id)">
            删除
          </el-button>
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
  name: 'UserManage',
  data() {
    return {
      users: [],
      page: 1,
      limit: 10,
      total: 0
    }
  },
  created() {
    this.loadUsers()
  },
  methods: {
    loadUsers() {
      const params = { page: this.page, limit: this.limit }
      this.$axios.get('/user/list', { params }).then(res => {
        this.users = res.data.records
        this.total = res.data.total
      })
    },
    deleteUser(id) {
      this.$confirm('确定删除该用户吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/user/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadUsers()
        })
      })
    },
    handlePageChange(page) {
      this.page = page
      this.loadUsers()
    }
  }
}
</script>

<style scoped>
.user-manage {
  background: white;
  padding: 20px;
  border-radius: 4px;
}
</style>


