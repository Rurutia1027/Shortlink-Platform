<template>
  <div>
    <el-form ref="ruleFormRef" :model="formData" :rules="formRule" label-width="80px">
      <el-form-item label="Redirect URL" prop="originUrl">
        <el-input v-model="formData.originUrl"
                  placeholder="Please enter a link starting with http:// or https:// or an app redirect URL"></el-input>
      </el-form-item>
      <el-form-item label="Description" prop="describe">
        <el-input
            :rows="4"
            v-model="formData.describe"
            type="textarea"
            placeholder="Create multiple short links by line breaks, one per line, up to 50 at once"
        />
        <span>{{ describeRows + '/' + maxDescribeRows }}</span>
      </el-form-item>

      <el-form-item label="Short Link Group" prop="gid">
        <el-select v-model="formData.gid" placeholder="Please select">
          <el-option v-for="item in groupInfo" :key="item.gid" :label="item.name" :value="item.gid"/>
        </el-select>
      </el-form-item>
      <el-form-item label="Validity Period" prop="v">
        <el-radio-group v-model="formData.validDateType">
          <el-radio :label="0">Permanent</el-radio>
          <el-radio :label="1">Custom</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="formData.validDateType === 1" label="Select Date">
        <el-date-picker
            :disabled-date="disabledDate"
            v-model="formData.validDate"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="datetime"
            placeholder="Select date"
            :shortcuts="shortcuts"
        />
        <span class="alert">After expiration, the link will automatically redirect to a 404 page!</span>
      </el-form-item>
      <el-form-item>
        <div style="width: 100%; display: flex; justify-content: flex-end;">
          <el-button class="buttons" type="primary" @click="onSubmit(ruleFormRef)">Confirm</el-button>
          <el-button class="buttons" @click="cancel">Cancel</el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import {getCurrentInstance, onBeforeUnmount, reactive, ref, watch} from 'vue'
import {useStore} from 'vuex'

const store = useStore()
const defaultDomain = store.state.domain ?? ' '
const props = defineProps({
  groupInfo: Array,
  editData: Object
})
const {proxy} = getCurrentInstance()
const API = proxy.$API
const editData = props.editData
// URL validation regex
const reg = /^(https?:\/\/(([a-zA-Z0-9]+-?)+[a-zA-Z0-9]+\.)+(([a-zA-Z0-9]+-?)+[a-zA-Z0-9]+))(:\d+)?(\/.*)?(\?.*)?(#.*)?$/;
console.log(editData)
// Date picker shortcuts for custom validity period
const shortcuts = [
  {
    text: 'One day',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24)
      return date
    },
  },
  {
    text: 'Seven days',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
      return date
    },
  },
  {
    text: 'Thirty days',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 30)
      return date
    },
  },
]

const groupInfo = ref()
const formData = reactive({
  domain: defaultDomain,
  originUrl: editData.originUrl,
  gid: editData.gid,
  createdType: editData.createdType,
  validDate: editData.validDate,
  describe: editData.describe,
  validDateType: editData.validDateType,
  fullShortUrl: editData.fullShortUrl
})
const initFormData = () => {
  formData.domain = defaultDomain
  formData.originUrl = null
  formData.createdType = 1
  formData.validDate = null
  formData.describe = null
  formData.validDateType = 0
  formData.fullShortUrl = null
}

const maxOriginUrlRows = ref(100) // Max allowed lines
const originUrlRows = ref(0)

// Debounce function
const fd = (fn, delay) => {
  let timer = null
  return function (url) {
    if (timer) {
      clearTimeout(timer)
      timer = null
    }
    timer = setTimeout(() => {
      fn(url)
    }, delay)
  }
}
// Query title from URL
const queryTitle = (url) => {
  if (reg.test(url)) {
    API.smallLinkPage.queryTitle({url: url}).then(res => {
      formData.describe = res?.data?.data
    })
  }
}
const getTitle = fd(queryTitle, 1000)
watch(
    () => formData.originUrl,
    nV => {
      originUrlRows.value = (nV || '').split(/\r|\r\n|\n/)?.length ?? 0
      // Only query title if description is empty
      if (!formData.describe) {
        getTitle(nV)
      }
    }
)

const maxDescribeRows = ref(100) // Max allowed lines for description
const describeRows = ref(0)
watch(
    () => formData.describe,
    nV => {
      describeRows.value = (nV || '').split(/\r|\r\n|\n/)?.length ?? 0
    }
)

// Set group info options and default select first
watch(
    () => props.groupInfo,
    nV => {
      groupInfo.value = nV
      formData.gid = nV[0].gid
    },
    {
      immediate: true
    }
)
watch(
    () => props.editData,
    nV => {
      console.log(nV)
      formData.originUrl = nV.originUrl
      formData.gid = nV.gid
      formData.createdType = nV.createdType
      formData.validDate = nV.validDate
      formData.describe = nV.describe
      formData.validDateType = nV.validDateType
      formData.fullShortUrl = nV.fullShortUrl
    }
)

// Validation rules
const formRule = reactive({
  originUrl: [
    {required: true, message: 'Please enter a URL', trigger: 'blur'},
    {
      validator: function (rule, value, callback) {
        if (value) {
          value.split(/\r|\r\n|\n/).forEach(item => {
            if (!reg.test(item)) {
              callback(new Error('Please enter a URL starting with http:// or https:// or app redirect URL'))
            }
          })
        }
        if (originUrlRows.value > maxOriginUrlRows.value) {
          callback(new Error('Exceeded max input of ' + maxOriginUrlRows.value + ' lines'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    },
  ],
  gid: [{required: true, message: 'Please select a group', trigger: 'blur'}],
  describe: [
    {required: true, message: 'Please enter a description', trigger: 'blur'},
    {
      validator: function (rule, value, callback) {
        if (describeRows.value > maxDescribeRows.value) {
          callback(new Error('Exceeded max input of ' + maxDescribeRows.value + ' lines'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    },
  ],
  validDate: [
    {required: false, message: 'Please enter a date', trigger: 'blur'}
  ]
})

// Disable past dates in date picker
const disabledDate = (time) => {
  return new Date(time).getTime() < new Date().getTime()
}

console.log(new Date().getTime())

// Emit events for confirm and cancel
const emits = defineEmits(['onSubmit', 'cancel'])

const ruleFormRef = ref()
const onSubmit = async (formEl) => {
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      emits('onSubmit', false)
      const res = await API.smallLinkPage.editSmallLink(formData)
      console.log('submit!', res)
    } else {
      console.log('error submit!', fields)
    }
  })
}
const cancel = () => {
  emits('cancel', false)
  initFormData()
}
onBeforeUnmount(() => {
  initFormData()
})
defineExpose({
  // Initialize form data when dialog is closed externally
  initFormData
})
</script>

<style lang="less" scoped>
.alert {
  color: rgb(231, 166, 67);
  font-size: 12px;
  width: 90%;
}
</style>