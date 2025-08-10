<template>
  <div style="display: flex; height: 100%; width: 100%;">
    <div class="options-box">
      <div>
        <span>Account Settings</span>
      </div>
    </div>
    <div class="main-box">
      <el-descriptions class="margin-top content-box" title="Personal Information" :column="1" :size="size" border>
        <template #extra>
        </template>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <user/>
              </el-icon>
              Username
            </div>
          </template>
          <span v-if="!dialogVisible">{{ userInfo?.data?.data?.username }}</span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <iphone/>
              </el-icon>
              Phone Number
            </div>
          </template>
          <span>{{ userInfo?.data?.data?.phone }}</span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <tickets/>
              </el-icon>
              Full Name
            </div>
          </template>
          <span>{{ userInfo?.data?.data?.realName }}</span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <Message/>
              </el-icon>
              Email
            </div>
          </template>
          <span>{{ userInfo?.data?.data?.mail }}</span>
        </el-descriptions-item>
      </el-descriptions>
      <el-button type="primary" @click="dialogVisible = !dialogVisible">Edit Personal Information</el-button>
    </div>
  </div>
  <!-- Edit Information -->
  <el-dialog v-model="dialogVisible" title="Edit Personal Information" width="60%" :before-close="handleClose">
    <div class="register" :class="{ hidden: isLogin }">
      <el-form ref="loginFormRef" :model="userInfoForm" label-width="50px" class="form-container" width="width"
               :rules="formRule">
        <el-form-item prop="username">
          <el-input v-model="userInfoForm.username" placeholder="Please enter username" maxlength="11" show-word-limit
                    disabled>
            <template v-slot:prepend> Username</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="mail">
          <el-input v-model="userInfoForm.mail" placeholder="Please enter email" show-word-limit clearable>
            <template v-slot:prepend> E<span class="second-font">mail</span></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="userInfoForm.phone" placeholder="Please enter phone number" show-word-limit clearable>
            <template v-slot:prepend> Phone Number</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="realName">
          <el-input v-model="userInfoForm.realName" placeholder="Please enter full name" show-word-limit clearable>
            <template v-slot:prepend> F<span class="second-font">ull Name</span></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="userInfoForm.password"
                    placeholder="Default password, enter new password if you want to change" show-word-limit clearable>
            <template v-slot:prepend> P<span class="second-font">assword</span></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <div style=" width: 100%;display: flex; justify-content: flex-end;">
            <el-button @click="dialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="changeUserInfo(loginFormRef)">
              Submit
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>

<script setup>
// (No Chinese comments here)
</script>

<style lang="scss" scoped>
.main-box {
  flex: 1;
  padding: 15px;
  background-color: rgb(238, 240, 245);
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
}

.content-box {
  flex: 1;
  background-color: white;
  padding: 20px;
}

.register {
  padding-right: 30px;
}

:deep(.el-descriptions__label) {
  width: 200px !important;
}

.second-font {
  margin-left: 13px;
}

.options-box {
  position: relative;
  height: 100%;
  width: 190px;
  border-right: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  padding-top: 15px;

  div {
    flex: 1;
    display: flex;
    height: 50px;
    align-items: center;
    justify-content: center;
    background-color: rgb(235, 239, 250);
    font-family: PingFangSC-Semibold, PingFang SC;
    color: #3464e0;
    font-weight: 600;
  }
}
</style>