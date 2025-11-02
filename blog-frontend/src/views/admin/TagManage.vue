<template>
  <div class="tag-manage">
    <div class="header">
      <h2>标签管理</h2>
      <el-button type="primary" @click="showDialog()">
        <i class="el-icon-plus"></i> 新建标签
      </el-button>
    </div>

    <el-table :data="tags" border style="margin-top: 20px">
      <el-table-column prop="name" label="标签名称"></el-table-column>
      <el-table-column prop="color" label="颜色" width="100">
        <template slot-scope="scope">
          <span :style="{color: scope.row.color}">{{ scope.row.color }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="showDialog(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="deleteTag(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="tagForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="tagForm.name"></el-input>
        </el-form-item>
        <el-form-item label="颜色">
          <el-input v-model="tagForm.color" placeholder="#409eff"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveTag">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'TagManage',
  data() {
    return {
      tags: [],
      dialogVisible: false,
      dialogTitle: '',
      tagForm: {
        id: null,
        name: '',
        color: ''
      }
    }
  },
  created() {
    this.loadTags()
  },
  methods: {
    loadTags() {
      this.$axios.get('/tag/list').then(res => {
        this.tags = res.data
      })
    },
    showDialog(row) {
      if (row) {
        this.dialogTitle = '编辑标签'
        this.tagForm = { ...row }
      } else {
        this.dialogTitle = '新建标签'
        this.tagForm = { id: null, name: '', color: '' }
      }
      this.dialogVisible = true
    },
    saveTag() {
      const api = this.tagForm.id 
        ? this.$axios.put('/tag', this.tagForm)
        : this.$axios.post('/tag', this.tagForm)
      
      api.then(() => {
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.loadTags()
      })
    },
    deleteTag(id) {
      this.$confirm('确定删除该标签吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/tag/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadTags()
        })
      })
    }
  }
}
</script>

<style scoped>
.tag-manage {
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


