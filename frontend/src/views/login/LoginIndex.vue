<template>
  <div class="login-page">
    <h1 class="title">Short Link Platform</h1>
    <div class="login-box">
      <!-- Login -->
      <div class="logon" :class="{ hidden: !isLogin }">
        <h2>User Login</h2>
        <el-form ref="loginFormRef1" :model="loginForm" label-width="50px" :rules="loginFormRule">
          <div class="form-container1">
            <el-form-item prop="phone">
              <el-input v-model="loginForm.username" placeholder="Username" maxlength="11" show-word-limit clearable>
                <template v-slot:prepend>Username</template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" clearable placeholder="Password" show-password
                        style="margin-top: 20px">
                <template v-slot:prepend>Password<span class="second-font"></span></template>
              </el-input>
            </el-form-item>
          </div>
          <div class="btn-group">
            <div>
              <el-checkbox class="remember-password" v-model="checked" style="color: #a0a0a0; margin: 0 0 0px 0">
                Remember Me
              </el-checkbox>
            </div>
            <div>
              <el-button :loading="loading" @keyup.enter="login" type="primary" plain @click="login(loginFormRef1)">
                Login
              </el-button>
            </div>
          </div>
        </el-form>
      </div>

      <!-- Register -->
      <div class="register" :class="{ hidden: isLogin }">
        <h2>Sign Up</h2>
        <el-form ref="loginFormRef2" :model="addForm" label-width="50px" class="form-container" width="width"
                 :rules="addFormRule">
          <el-form-item prop="username">
            <el-input v-model="addForm.username" placeholder="Username" maxlength="11" show-word-limit clearable>
              <template v-slot:prepend>Username</template>
            </el-input>
          </el-form-item>
          <el-form-item prop="mail">
            <el-input v-model="addForm.mail" placeholder="Email" show-word-limit clearable>
              <template v-slot:prepend>Email</template>
            </el-input>
          </el-form-item>
          <el-form-item prop="phone">
            <el-input v-model="addForm.phone" placeholder="Phone Number" show-word-limit clearable>
              <template v-slot:prepend>Phone Number</template>
            </el-input>
          </el-form-item>
          <el-form-item prop="realName">
            <el-input v-model="addForm.realName" placeholder="Username" show-word-limit clearable>
              <template v-slot:prepend>Username</template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input v-model="addForm.password" type="password" clearable placeholder="Password" show-password>
              <template v-slot:prepend>Password</template>
            </el-input>
          </el-form-item>
          <div class="btn-group">
            <div></div>
            <div>
              <el-button :loading="loading" @keyup.enter="login" type="primary" plain @click="addUser(loginFormRef2)">
                Sign Up
              </el-button>
            </div>
          </div>
        </el-form>
      </div>
      <!-- Toggle button -->
      <div class="move" ref="moveRef">
        <span style="font-size: 18px; margin-bottom: 25px; color: rgb(225, 238, 250)">{{
            !isLogin ? 'Already have an account?' : "Don't have an account yet?"
          }}</span>
        <span style="font-size: 16px; color: rgb(225, 238, 250)">{{
            !isLogin ? 'Welcome back! Please log in.' : 'Welcome! Please sign up.'
          }}</span>
        <el-button style="width: 100px; margin-top: 30px" @click="changeLogin">{{
            !isLogin ? 'Go to Login' : 'Go to Sign Up'
          }}
        </el-button>
      </div>
    </div>
    <div ref="vantaRef" class="vanta"></div>
  </div>
</template>

<script setup>
import {getUsername, setToken, setUsername} from '@/core/auth.js'
import {getCurrentInstance, onBeforeUnmount, onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import * as THREE from 'three'
import WAVES from 'vanta/src/vanta.waves'

const {proxy} = getCurrentInstance()
const API = proxy.$API
const loginFormRef1 = ref()
const loginFormRef2 = ref()
const router = useRouter()

const loginForm = reactive({
  username: '',
  password: '',
})

const addForm = reactive({
  username: '',
  password: '',
  realName: '',
  phone: '',
  mail: ''
})

const addFormRule = reactive({
  phone: [
    {required: true, message: 'Please enter phone number', trigger: 'blur'},
    {
      pattern: /^1[3|5|7|8|9]\d{9}$/,
      message: 'Please enter a valid phone number',
      trigger: 'blur'
    },
    {min: 11, max: 11, message: 'Phone number must be 11 digits', trigger: 'blur'}
  ],
  username: [{required: true, message: 'Please enter your real name', trigger: 'blur'}],
  password: [
    {required: true, message: 'Please enter a password', trigger: 'blur'},
    {min: 8, max: 15, message: 'Password length should be at least 8 characters', trigger: 'blur'}
  ],
  mail: [
    {required: true, message: 'Please enter an email', trigger: 'blur'},
    {
      pattern: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
      message: 'Please enter a valid email address',
      trigger: 'blur'
    }
  ],
  realNamee: [
    {required: true, message: 'Please enter your name', trigger: 'blur'},
  ]
})

const loginFormRule = reactive({
  username: [{required: true, message: 'Please enter your real name', trigger: 'blur'}],
  password: [
    {required: true, message: 'Please enter a password', trigger: 'blur'},
    {min: 8, max: 15, message: 'Password length should be at least 8 characters', trigger: 'blur'}
  ],
})

// Register user
const addUser = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      // Check if username already exists
      const res1 = await API.user.hasUsername({username: addForm.username})
      if (res1.data.success !== false) {
        // Register
        const res2 = await API.user.addUser(addForm)
        console.log(res2.data.success)
        if (res2.data.success === false) {
          ElMessage.warning(res2.data.message)
        } else {
          const res3 = await API.user.login({username: addForm.username, password: addForm.password})
          const token = res3?.data?.data?.token
          console.log("here we got token " + token)
          // Save username and token in cookies and localStorage
          if (token) {
            setToken(token)
            setUsername(addForm.username)
            localStorage.setItem('token', token)
            localStorage.setItem('username', addForm.username)
          }
          ElMessage.success('Registration and login successful!')
          router.push('/home')
        }
      } else {
        ElMessage.warning('Username already exists!')
      }
    } else {
      return false
    }
  })
}

// Login user
const login = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      const res1 = await API.user.login(loginForm)
      console.log("response data: " +res1.data.code)
      if (res1.data.code === '0') {
        const token = res1?.data?.data?.token
        // Save username and token in cookies and localStorage
        if (token) {
          setToken(token)
          setUsername(loginForm.username)
          localStorage.setItem('token', token)
          localStorage.setItem('username', loginForm.username)
        }
        ElMessage.success('Login successful!')
        router.push('/home')
      } else if (res1.data.message === 'User Already Login') {
        const cookiesUsername = getUsername()
        if (cookiesUsername === loginForm.username) {
          ElMessage.success('Login successful!')
          router.push('/home')
        } else {
          ElMessage.warning('User already logged in on other devices. Please do not re-login!')
        }
      } else if (res1.data.message === 'User Not Exist') {
        ElMessage.error('Incorrect password!')
      }
    } else {
      return false
    }
  })
}

const loading = ref(false)
// remember password or not
const checked = ref(true)
const vantaRef = ref()
// background animation
let vantaEffect = null
onMounted(() => {
  vantaEffect = WAVES({
    el: vantaRef.value,
    THREE: THREE,
    mouseControls: true,
    touchControls: true,
    gyroControls: false,
    minHeight: 200.0,
    minWidth: 200.0,
    scale: 1.0,
    scaleMobile: 1.0
  })
})
onBeforeUnmount(() => {
  if (vantaEffect) {
    vantaEffect.destroy()
  }
})

const isLogin = ref(true)
const moveRef = ref()
const changeLogin = () => {
  isLogin.value = !isLogin.value
  if (isLogin.value) {
    moveRef.value.style.transform = 'translate(0, 0)'
  } else {
    moveRef.value.style.transform = 'translate(-420px, 0)'
  }
}
</script>

<style lang="less" scoped>
.login-box {
  border: 2px solid #0984e3;
  overflow: hidden;
  display: flex;
  justify-content: space-between;
  border-radius: 20px;
  padding: 0 40px 0 40px;
  width: 700px;
  // background-color: #eee;
  position: absolute;
  z-index: 999;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-sizing: border-box;
  // border: 1px solid #eee;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  background-color: #fff;
  animation: hideIndex 0.5s;

  h2 {
    font-size: 30px;
    font-family: PingFangSC-Semibold,
    PingFang SC;
    font-weight: 600;
    color: #3a3f63;
    width: 100%;
    text-align: center;
    padding: 20px;
  }

  .el-form-item {
    margin-bottom: 23px;
  }

  .btn-group {
    margin-top: 30px;
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;

    .el-button {
      width: 100px;
    }

    .remeber-password {
      left: 0;
      line-height: 0.5rem;
    }
  }

  .el-checkbox {
    width: 100%;
    text-align: center;
    margin-top: 1rem;
  }
}

/deep/ .el-form-item__content {
  margin-left: 0 !important;
}

@keyframes hideIndex {
  0% {
    opacity: 0;
    transform: translate(7.3125rem, -50%);
  }

  100% {
    opacity: 1;
    transform: translate(-50%, -50%);
  }
}

.login-page {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

.vanta {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
  z-index: 0;
}

.logon {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.hidden {
  animation: hidden 1s;
  animation-fill-mode: forwards;
}

@keyframes hidden {

  0% {
    opacity: 1;
  }

  70% {
    opacity: 0;
  }

  100% {
    opacity: 0;
  }
}

.move {
  position: absolute;
  right: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 40%;
  transition-duration: 0.5s;
  align-items: center;
  background: #06beb6;
  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #0984e3, #0984e3);
  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right,
  #1a8fd5,
  #0984e3);
  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
}

.title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  top: 15%;
  z-index: 999;
  font-size: 40px;
  color: #fff;
  font-weight: bolder;
}

:deep(.el-input__suffix-inner) {
  width: 60px;
}

.form-container1 {
  transform: translateY(-80%);
}

.second-font {
  margin-left: 13px;
}

.verification-flex {
  display: flex;
  flex-direction: column;
  align-items: flex-start;

  .img {
    margin-top: 10px;
    align-self: center;
  }

  .form {
    transform: translateY(15px);
    width: 90%;
  }
}
</style>
