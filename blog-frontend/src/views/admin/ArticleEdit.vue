<template>
  <div class="article-edit">
    <h2>{{ isEdit ? '编辑文章' : '新建文章' }}</h2>
    
    <el-form :model="articleForm" label-width="100px" style="margin-top: 20px">
      <el-form-item label="标题">
        <el-input v-model="articleForm.title" placeholder="请输入文章标题"></el-input>
      </el-form-item>
      
      <el-form-item label="分类">
        <el-select v-model="articleForm.categoryId" placeholder="请选择分类">
          <el-option 
            v-for="cat in categories" 
            :key="cat.id" 
            :label="cat.name" 
            :value="cat.id">
          </el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="标签">
        <el-select v-model="articleForm.tagIds" multiple placeholder="请选择标签">
          <el-option 
            v-for="tag in tags" 
            :key="tag.id" 
            :label="tag.name" 
            :value="tag.id">
          </el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="摘要">
        <el-input 
          type="textarea" 
          v-model="articleForm.summary" 
          :rows="3"
          placeholder="请输入文章摘要">
        </el-input>
      </el-form-item>
      
      <el-form-item label="内容">
        <mavon-editor 
          ref="mdEditor"
          v-model="articleForm.content" 
          :toolbars="toolbars"
          @imgAdd="handleImgAdd"
          style="min-height: 500px">
        </mavon-editor>
      </el-form-item>
      
      <el-form-item label="封面图">
        <div style="margin-bottom: 10px;">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleCoverUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :show-file-list="false"
            accept="image/*">
            <el-button size="small" type="primary">
              <i class="el-icon-upload"></i> 上传封面图
            </el-button>
            <div slot="tip" class="el-upload__tip" style="margin-left: 10px; display: inline-block;">
              支持jpg/png/gif格式，文件大小不超过2MB
            </div>
          </el-upload>
        </div>
        <div v-if="articleForm.coverImage" style="margin-top: 10px;">
          <img :src="getImageUrl(articleForm.coverImage)" style="max-width: 500px; max-height: 300px; border-radius: 4px; border: 1px solid #ddd;"/>
          <div style="margin-top: 5px;">
            <el-button size="mini" type="text" @click="articleForm.coverImage = ''">删除封面图</el-button>
          </div>
        </div>
      </el-form-item>
      
      <el-form-item label="设置">
        <el-checkbox v-model="articleForm.isTop" :true-label="1" :false-label="0">置顶</el-checkbox>
        <el-checkbox v-model="articleForm.isRecommend" :true-label="1" :false-label="0">推荐</el-checkbox>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="saveArticle(2)">发布</el-button>
        <el-button @click="saveArticle(0)">保存草稿</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'ArticleEdit',
  data() {
    return {
      articleForm: {
        title: '',
        categoryId: null,
        tagIds: [],
        summary: '',
        content: '',
        coverImage: '',
        isTop: 0,
        isRecommend: 0,
        status: 2
      },
      categories: [],
      tags: [],
      isEdit: false,
      uploadUrl: '/api/file/upload',
      toolbars: {
        bold: true,
        italic: true,
        header: true,
        underline: true,
        strikethrough: true,
        mark: true,
        superscript: true,
        subscript: true,
        quote: true,
        ol: true,
        ul: true,
        link: true,
        imagelink: true,
        code: true,
        table: true,
        fullscreen: true,
        readmodel: true,
        htmlcode: true,
        help: true,
        undo: true,
        redo: true,
        trash: true,
        save: true,
        navigation: true,
        alignleft: true,
        aligncenter: true,
        alignright: true,
        subfield: true,
        preview: true
      }
    }
  },
  computed: {
    uploadHeaders() {
      const token = localStorage.getItem('token')
      return {
        'Authorization': token ? `Bearer ${token}` : ''
      }
    }
  },
  created() {
    this.loadCategories()
    this.loadTags()
    
    const id = this.$route.params.id
    if (id) {
      this.isEdit = true
      this.loadArticle(id)
    }
  },
  methods: {
    loadCategories() {
      this.$axios.get('/category/list').then(res => {
        this.categories = res.data
      })
    },
    loadTags() {
      this.$axios.get('/tag/list').then(res => {
        this.tags = res.data
      })
    },
    loadArticle(id) {
      this.$axios.get(`/article/${id}/info`).then(res => {
        const article = res.data
        this.articleForm = {
          title: article.title,
          categoryId: article.categoryId,
          tagIds: article.tags ? article.tags.map(t => t.id) : [],
          summary: article.summary,
          content: article.content,
          coverImage: article.coverImage,
          isTop: article.isTop,
          isRecommend: article.isRecommend,
          status: article.status
        }
      }).catch(err => {
        this.$message.error('加载文章失败')
        this.$router.back()
      })
    },
    saveArticle(status) {
      this.articleForm.status = status
      
      if (!this.articleForm.title || !this.articleForm.content) {
        this.$message.warning('请填写标题和内容')
        return
      }
      
      const api = this.isEdit 
        ? this.$axios.put(`/article/${this.$route.params.id}`, this.articleForm)
        : this.$axios.post('/article', this.articleForm)
      
      api.then((res) => {
        this.$message.success(res.message || '操作成功')
        this.$router.push('/admin/articles')
      })
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
    handleCoverUploadSuccess(response) {
      if (response.code === 200) {
        this.articleForm.coverImage = response.data
        this.$message.success('封面图上传成功!')
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    handleUploadError() {
      this.$message.error('图片上传失败，请重试')
    },
    async handleImgAdd(pos, file) {
      // 创建FormData对象
      const formData = new FormData()
      formData.append('file', file)
      
      try {
        // 上传图片到服务器
        const response = await this.$axios.post('/file/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        
        if (response.code === 200 && response.data) {
          // 获取上传后的文件路径
          const url = response.data
          
          // 使用 mavon-editor 的方法将图片插入到编辑器
          this.$refs.mdEditor.$img2Url(pos, url)
          this.$message.success('图片上传成功!')
        } else {
          this.$message.error(response.message || '图片上传失败')
        }
      } catch (error) {
        console.error('图片上传失败:', error)
        this.$message.error('图片上传失败，请重试')
      }
    },
    getImageUrl(url) {
      if (!url) return ''
      // 如果是完整URL，直接返回
      if (url.startsWith('http://') || url.startsWith('https://')) {
        return url
      }
      // 如果是纯数字，可能是文件ID，通过ID下载文件
      if (/^\d+$/.test(url)) {
        return `/api/file/download/${url}`
      }
      // 如果是相对路径，拼接文件访问地址
      if (url.startsWith('/files/')) {
        return `/api${url}`
      }
      // 如果是文件路径（如 C:\...），需要通过文件服务访问
      if (url.indexOf('\\') >= 0 || url.indexOf('/') === 0) {
        const fileName = url.split(/[/\\]/).pop()
        return `/api/files/${fileName}`
      }
      return url
    }
  }
}
</script>

<style scoped>
.article-edit {
  background: white;
  padding: 20px;
  border-radius: 4px;
}
</style>


