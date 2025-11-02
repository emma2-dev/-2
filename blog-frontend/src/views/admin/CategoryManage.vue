<template>
  <div class="category-manage">
    <div class="header">
      <h2>分类管理</h2>
      <el-button type="primary" @click="showDialog()">
        <i class="el-icon-plus"></i> 新建分类
      </el-button>
    </div>

    <el-table :data="categories" border style="margin-top: 20px">
      <el-table-column prop="name" label="分类名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="sort" label="排序" width="100"></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="showDialog(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="deleteCategory(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="categoryForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="categoryForm.name"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="categoryForm.description"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="categoryForm.sort" :min="0"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCategory">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CategoryManage',
  data() {
    return {
      categories: [],
      dialogVisible: false,
      dialogTitle: '',
      categoryForm: {
        id: null,
        name: '',
        description: '',
        sort: 0
      }
    }
  },
  created() {
    this.loadCategories()
  },
  methods: {
    loadCategories() {
      this.$axios.get('/category/list').then(res => {
        this.categories = res.data
      })
    },
    showDialog(row) {
      if (row) {
        this.dialogTitle = '编辑分类'
        this.categoryForm = { ...row }
      } else {
        this.dialogTitle = '新建分类'
        this.categoryForm = { id: null, name: '', description: '', sort: 0 }
      }
      this.dialogVisible = true
    },
    saveCategory() {
      const api = this.categoryForm.id 
        ? this.$axios.put('/category', this.categoryForm)
        : this.$axios.post('/category', this.categoryForm)
      
      api.then(() => {
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.loadCategories()
      })
    },
    deleteCategory(id) {
      this.$confirm('确定删除该分类吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/category/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadCategories()
        })
      })
    }
  }
}
</script>

<style scoped>
.category-manage {
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


