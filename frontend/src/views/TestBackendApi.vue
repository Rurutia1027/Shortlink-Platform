<template>
  <div>
    <h1>API Test Page</h1>
    <el-button @click="testApi">Test Spring Boot Controller</el-button>
    <pre>{{ result }}</pre>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import axios from 'axios' // If you don't already have axios, install it

const result = ref(null)

const testApi = async () => {
  try {
    const res = await axios.get('/api/short-link/admin/v1/tests')
    result.value = res.data
    console.log('Response Data：', res.data)
  } catch (err) {
    result.value = err.message
    console.error('Response Err：', err)
  }
}
</script>
