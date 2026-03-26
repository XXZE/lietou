<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">
      <div class="title-container">
        <h3>管理平台</h3>
      </div>

      <el-form-item prop="username">
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="用户名"
          name="username"
          type="text"
          tabindex="1"
          autocomplete="on"
          prefix-icon="el-icon-user"
        />
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          ref="password"
          v-model="loginForm.password"
          type="password"
          placeholder="密码"
          name="password"
          tabindex="2"
          autocomplete="on"
          prefix-icon="el-icon-lock"
          @keyup.enter.native="handleLogin"
        />
      </el-form-item>

      <el-button :loading="loading" type="primary" @click.native.prevent="handleLogin">
        登 录
      </el-button>
    </el-form>
  </div>
</template>

<script>
import { login } from '@/api/login'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: 'admin',
        password: 'admin123'
      },
      loginRules: {
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
          login(this.loginForm).then(response => {
            if (response.code === 200) {
              this.$store.dispatch('login', response.data)
              this.$message.success('登录成功')
              this.$router.push({ path: '/' })
            } else {
              this.$message.error(response.message || '登录失败')
            }
          }).catch(() => {
            this.$message.error('登录失败，请稍后重试')
          }).finally(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>
