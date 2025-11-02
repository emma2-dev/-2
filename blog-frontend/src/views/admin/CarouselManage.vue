<template>
  <div class="carousel-manage">
    <div class="header">
      <h2>轮播图管理</h2>
      <el-button type="primary" @click="openDialog()">
        <i class="el-icon-plus"></i> 新增轮播图
      </el-button>
    </div>

    <el-table :data="list" border style="margin-top: 16px">
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column label="图片" width="220">
        <template slot-scope="scope">
          <img :src="getImageUrl(scope.row.imgUrl)" style="height:60px;width:100px;object-fit:cover;border-radius:4px;cursor:pointer;" @click="previewImage(scope.row.imgUrl)"/>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" width="200"/>
      <el-table-column prop="targetUrl" label="跳转链接"/>
      <el-table-column prop="sort" label="排序" width="100"/>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '显示' : '隐藏' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="openDialog(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="visible" width="600px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="图片">
          <div style="margin-bottom: 10px;">
            <el-upload
              :action="uploadUrl"
              :headers="uploadHeaders"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :before-upload="beforeUpload"
              :show-file-list="false"
              accept="image/*">
              <el-button size="small" type="primary">
                <i class="el-icon-upload"></i> 点击上传
              </el-button>
              <div slot="tip" class="el-upload__tip" style="margin-top: 10px;">
                支持jpg/png/gif格式，文件大小不超过2MB
              </div>
            </el-upload>
          </div>
          <div v-if="form.imgUrl" style="margin-top: 10px;">
            <img :src="getImageUrl(form.imgUrl)" style="max-width: 100%; max-height: 200px; border-radius: 4px; border: 1px solid #ddd;"/>
            <div style="margin-top: 5px;">
              <el-button size="mini" type="text" @click="form.imgUrl = ''">删除图片</el-button>
            </div>
          </div>
          <el-input 
            v-model="form.imgUrl" 
            placeholder="或直接输入图片URL" 
            style="margin-top: 10px;">
            <template slot="prepend">URL</template>
          </el-input>
        </el-form-item>
        <el-form-item label="跳转链接">
          <el-input v-model="form.targetUrl" placeholder="可选，点击轮播图后跳转的链接"></el-input>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="可选，轮播图标题"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="999"></el-input-number>
          <span style="margin-left: 10px; color: #909399; font-size: 12px;">数字越大越靠前</span>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="statusSwitch" active-text="显示" inactive-text="隐藏"></el-switch>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="visible=false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CarouselManage',
  data() {
    return {
      list: [],
      visible: false,
      dialogTitle: '新增轮播图',
      form: {
        id: null,
        imgUrl: '',
        targetUrl: '',
        title: '',
        sort: 0,
        status: 1
      },
      uploadUrl: '/api/file/upload'
    }
  },
  computed: {
    statusSwitch: {
      get() { return this.form.status === 1 },
      set(v) { this.form.status = v ? 1 : 0 }
    },
    uploadHeaders() {
      const token = localStorage.getItem('token')
      return {
        'Authorization': token ? `Bearer ${token}` : ''
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      // 管理员查询全部
      this.$axios.get('/carousel/all').then(res => {
        this.list = res.data
      })
    },
    openDialog(row) {
      if (row) {
        this.dialogTitle = '编辑轮播图'
        this.form = { ...row }
      } else {
        this.dialogTitle = '新增轮播图'
        this.form = { id: null, imgUrl: '', targetUrl: '', title: '', sort: 0, status: 1 }
      }
      this.visible = true
    },
    beforeUpload(file) {
      const isImage = file.type.indexOf('image/') === 0
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
        return false
      }
      return true
    },
    handleUploadSuccess(response) {
      if (response.code === 200) {
        this.form.imgUrl = response.data
        this.$message.success('图片上传成功!')
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    handleUploadError() {
      this.$message.error('图片上传失败，请重试')
    },
    getImageUrl(url) {
      if (!url) return ''
      // 如果是完整URL，直接返回
      if (url.startsWith('http://') || url.startsWith('https://')) {
        return url
      }
      // 如果是相对路径，拼接文件访问地址
      if (url.startsWith('/files/')) {
        return `/api${url}`
      }
      // 如果是文件路径（如 C:\...），需要通过文件服务访问
      if (url.indexOf('\\') >= 0 || url.indexOf('/') === 0) {
        // 提取文件名，通过文件服务访问
        const fileName = url.split(/[/\\]/).pop()
        return `/api/files/${fileName}`
      }
      return url
    },
    submit() {
      if (!this.form.imgUrl) {
        this.$message.warning('请上传图片或输入图片URL')
        return
      }
      const api = this.form.id ? this.$axios.put('/carousel', this.form) : this.$axios.post('/carousel', this.form)
      api.then(() => {
        this.$message.success('保存成功')
        this.visible = false
        this.load()
      }).catch(err => {
        this.$message.error(err.message || '保存失败')
      })
    },
    remove(id) {
      this.$confirm('确定删除该轮播图吗？', '提示', { type: 'warning' }).then(() => {
        this.$axios.delete(`/carousel/${id}`).then(() => {
          this.$message.success('删除成功')
          this.load()
        })
      })
    },
    previewImage(url) {
      const imageUrl = this.getImageUrl(url)
      window.open(imageUrl, '_blank')
    }
  }
}
</script>

<style scoped>
.carousel-manage { background: #fff; padding: 16px; border-radius: 4px; }
.header { display: flex; justify-content: space-between; align-items: center; }
.header h2 { margin: 0; }
</style>
