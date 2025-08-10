<template>
  <div class="common-layout">
    <el-container>
      <el-header height="50px" style="padding: 0">
        <div class="header">
          <div @click="toMySpace" class="logo">SaaS-ShortLink</div>
          <div style="display: flex; align-items: center">
            <span class="link-span">Doc</span>
            <span class="link-span">Blog</span>
            <span class="link-span">Community</span>
            <el-dropdown>
              <div class="block">
                <el-avatar :size="30" class="avatar" :style="`background:${extractColorByName(firstName)}`">{{ firstName }}
                </el-avatar>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="toMine">Profile</el-dropdown-item>
                  <el-dropdown-item divided @click="logout">Exit</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main style="padding: 0">
        <div class="content-box">
          <RouterView class="content-space" />
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { removeKey, removeUsername, getToken, getUsername } from '@/core/auth.js'
import { ElMessage } from 'element-plus'
const { proxy } = getCurrentInstance()
const API = proxy.$API
const router = useRouter()
const squareUrl = ref('https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png')
const toMine = () => {
  router.push('/home' + '/mine')
}

// Logout
const logout = async () => {
  const token = getToken()
  const username = getUsername()
  // Logout API
  await API.user.logout({ token, username })
  // Remove token & username from cookies
  removeUsername()
  removeKey()
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/')
  ElMessage.success('Logout！')
}

const toMySpace = () => {
  router.push('/home' + '/mySpace')
}
const firstName = ref('')
onMounted(async () => {
  const username = getUsername()
  const res = await API.user.queryUserInfo(username)
  firstName.value = res?.data?.data?.realName?.split('')[0]
})

const extractColorByName = (name) => {
  var temp = [];
  temp.push("#");
  for (let index = 0; index < name.length; index++) {
    temp.push(parseInt(name[index].charCodeAt(0), 10).toString(16));
  }
  return temp.slice(0, 5).join('').slice(0, 4);
}
</script>

<style lang="scss" scoped>
.el-container {
  height: 100vh;

  .el-aside {
    border: 0;
    background-color: #0e5782;

    ul {
      border: 0px;
    }
  }

  .el-main {
    background-color: #e8e8e8;
  }
}

.header {
  background-color: #2550cd;
  padding: 0 30px 0 20px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .block {
    margin-top: 5px;
    cursor: pointer;
    display: flex;
    align-items: center;
    border: 0px;
  }
}

.content-box {
  height: calc(100vh - 50px);
  background-color: white;
}

:deep(.el-tooltip__trigger:focus-visible) {
  outline: unset;
}

.logo {
  font-size: 15px;
  font-weight: 600;
  color: #e8e8e8;
  font-family: Helvetica, Tahoma, Arial, "PingFang SC", "Hiragino Sans GB", "Heiti SC", "Microsoft YaHei", "WenQuanYi Micro Hei";
  // font-family: 'Helvetica Neue', Helvetica, STHeiTi, Arial, sans-serif;
  cursor: pointer;
}

.logo:hover {
  color: #82b1cc;
}

.link-span {
  color: #e8e8e8;
  margin-right: 30px;
  font-size: 16px;
  font-family: 'Helvetica Neue', Helvetica, STHeiTi, Arial, sans-serif;
  cursor: pointer;
}

.link-span:hover {
  color: #80b0cb;
}

.avatar {
  transform: translateY(-2px);
}
</style>
