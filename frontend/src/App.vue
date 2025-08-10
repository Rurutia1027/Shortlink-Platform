<script setup>
import { removeKey } from '@/core/auth.js'
const removeCookies = () => {
  removeKey('Authorization')
}
</script>

<template>
  <el-config-provider>
    <router-view />
  </el-config-provider>
</template>

<style scoped lang="less">
a {
  text-decoration: none !important;
}

</style>
