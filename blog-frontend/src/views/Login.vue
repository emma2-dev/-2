<template>
  <div class="login-container">
    <div class="particles">
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
    </div>
    <div class="login-box">
      <h2 class="title">用户登录</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginForm">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名"
            prefix-icon="el-icon-user">
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            @keyup.enter.native="handleLogin">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="footer">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('login', this.loginForm).then(() => {
            this.$message.success('登录成功')
            this.$router.push('/home')
          }).finally(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: #A0D5F2;
}

/* 科技感背景动画 */
.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: 
    radial-gradient(circle at 20% 50%, rgba(67, 85, 149, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(63, 68, 134, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 40% 20%, rgba(67, 85, 149, 0.1) 0%, transparent 50%);
  animation: backgroundShift 15s ease infinite;
}

@keyframes backgroundShift {
  0%, 100% {
    transform: translateX(0) translateY(0);
  }
  33% {
    transform: translateX(20px) translateY(-20px);
  }
  66% {
    transform: translateX(-20px) translateY(20px);
  }
}

/* 网格背景 */
.login-container::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(67, 85, 149, 0.1) 1px, transparent 1px),
    linear-gradient(90deg, rgba(67, 85, 149, 0.1) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridMove 20s linear infinite;
}

@keyframes gridMove {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(50px, 50px);
  }
}

/* 科技光效粒子 */
.login-container .particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.particles span {
  position: absolute;
  display: block;
  width: 2px;
  height: 2px;
  background: rgba(63, 68, 134, 0.8);
  border-radius: 50%;
  animation: float 15s infinite;
  box-shadow: 0 0 10px rgba(63, 68, 134, 0.5);
}

.particles span:nth-child(1) { left: 10%; animation-delay: 0s; animation-duration: 12s; }
.particles span:nth-child(2) { left: 20%; animation-delay: 2s; animation-duration: 10s; }
.particles span:nth-child(3) { left: 30%; animation-delay: 4s; animation-duration: 15s; }
.particles span:nth-child(4) { left: 40%; animation-delay: 1s; animation-duration: 11s; }
.particles span:nth-child(5) { left: 50%; animation-delay: 3s; animation-duration: 13s; }
.particles span:nth-child(6) { left: 60%; animation-delay: 5s; animation-duration: 14s; }
.particles span:nth-child(7) { left: 70%; animation-delay: 0.5s; animation-duration: 12s; }
.particles span:nth-child(8) { left: 80%; animation-delay: 2.5s; animation-duration: 10s; }
.particles span:nth-child(9) { left: 90%; animation-delay: 4.5s; animation-duration: 16s; }

@keyframes float {
  0% {
    transform: translateY(100vh) scale(0);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100vh) scale(1);
    opacity: 0;
  }
}

.login-box {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.3),
    0 0 50px rgba(67, 85, 149, 0.2),
    inset 0 1px 1px rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 2;
  animation: fadeInUp 0.8s ease;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 28px;
  font-weight: bold;
  background: linear-gradient(135deg, #435595 0%, #3F4486 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.footer {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.footer a {
  color: #435595;
  text-decoration: none;
  font-weight: 500;
}

.footer a:hover {
  text-decoration: underline;
  color: #3F4486;
}

/* 输入框科技感增强 */
::v-deep .el-input__inner {
  border-radius: 8px;
  transition: all 0.3s;
}

::v-deep .el-input__inner:focus {
  border-color: #435595;
  box-shadow: 0 0 10px rgba(67, 85, 149, 0.3);
}

/* 按钮科技感增强 */
::v-deep .el-button--primary {
  border-radius: 8px;
  transition: all 0.3s;
  box-shadow: 0 4px 15px rgba(67, 85, 149, 0.4);
  background: #435595;
  border-color: #435595;
}

::v-deep .el-button--primary:hover {
  box-shadow: 0 6px 20px rgba(67, 85, 149, 0.6);
  transform: translateY(-2px);
  background: #3F4486;
  border-color: #3F4486;
}
</style>


