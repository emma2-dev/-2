<template>
  <div class="user-center">
    <div class="header">
      <div class="container">
        <h1 class="logo" @click="$router.push('/home')">个人博客</h1>
      </div>
    </div>

    <div class="container main-content">
      <el-card class="user-card">
        <h2>个人信息</h2>
        <el-form :model="userForm" label-width="100px">
          <el-form-item label="用户名">
            <el-input v-model="userForm.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="userForm.nickname"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="userForm.email"></el-input>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="userForm.phone"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="userForm.gender">
              <el-radio :label="0">未知</el-radio>
              <el-radio :label="1">男</el-radio>
              <el-radio :label="2">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="个人简介">
            <el-input type="textarea" v-model="userForm.introduction" :rows="4"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updateUserInfo">保存修改</el-button>
          </el-form-item>
        </el-form>

        <el-divider></el-divider>

        <h2>修改密码</h2>
        <el-form :model="passwordForm" label-width="100px">
          <el-form-item label="原密码">
            <el-input type="password" v-model="passwordForm.oldPassword"></el-input>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input type="password" v-model="passwordForm.newPassword"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updatePassword">修改密码</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserCenter',
  data() {
    return {
      userForm: {
        username: '',
        nickname: '',
        email: '',
        phone: '',
        gender: 0,
        introduction: ''
      },
      passwordForm: {
        oldPassword: '',
        newPassword: ''
      }
    }
  },
  created() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      this.$axios.get('/user/info').then(res => {
        this.userForm = res.data
      })
    },
    updateUserInfo() {
      this.$axios.put('/user/update', this.userForm).then(() => {
        this.$message.success('更新成功')
        this.loadUserInfo()
      })
    },
    updatePassword() {
      if (!this.passwordForm.oldPassword || !this.passwordForm.newPassword) {
        this.$message.warning('请填写完整')
        return
      }
      const params = {
        oldPassword: this.passwordForm.oldPassword,
        newPassword: this.passwordForm.newPassword
      }
      this.$axios.put('/user/password', null, { params }).then(() => {
        this.$message.success('密码修改成功')
        this.passwordForm = { oldPassword: '', newPassword: '' }
      })
    }
  }
}
</script>

<style scoped>
.header {
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  padding: 20px 0;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.logo {
  font-size: 28px;
  font-weight: bold;
  color: #435595;
  margin: 0;
  cursor: pointer;
}

.main-content {
  margin-top: 30px;
}

.user-card {
  padding: 20px;
}

.user-card h2 {
  margin-bottom: 20px;
}
</style>


