<template>
  <div style="display: flex; height: 100%">
    <div class="options-box">
      <div class="option-title flex-box">
        <div>
          Short Link Group <span> Total {{ editableTabs?.length }}}</span>
        </div>
      </div>
    </div>
  </div>
</template>


<!-- Page Variables -->
<script setup>
import { ref, reactive, onMounted, getCurrentInstance, watch, nextTick } from 'vue'
import {getTodayFormatDate, getLastWeekFormatDate} from '@/utils/plugins.js'
import Sortable from 'sortablejs'

const nums = ref(0)
const favicon1 = ref()
const originUrl1 = ref()
const orderIndex = ref(0)
const { proxy } = getCurrentInstance()
const API = proxy.$API
const chartsInfoRef = ref()
const chartsInfo = ref()
const tableInfo = ref()
const createLink1Ref = ref()
const createLink2Ref = ref()
let selectedIndex = ref(0)
const editableTabs = ref([])

const afterAddLink = () => {
  setTimeout(() => {
    getGroupInfo(queryPage)
    document.querySelector('.addButton') && document.querySelector('.addButton').blur()
  }, 0)
  if (createLink1Ref.value) {
    createLink1Ref.value.initFormData()
  }
  if (createLink2Ref.value) {
    createLink2Ref.value.initFormData()
  }
  if (editLinkRef.value) {
    editLinkRef.value.initFormData()
  }
}

const statsFormData = reactive({
  endDate: getTodayFormatDate(),
  startDate: getLastWeekFormatDate(),
  size: 10,
  current: 1
})

const initStatsFormData = () => {
  statsFormData.endDate = getTodayFormatDate()
  statsFormData.startDate = getLastWeekFormatDate()
}

const visitLink = {
  fullShortUrl: '',
  gid: ''
}

const isGroup = ref(false)
const tableFullShortUrl = ref()
const tableGid = ref()

const chartsVisible = async (rowInfo, dateList) => {
  chartsInfoTitle.value = rowInfo?.describe
  const {fullShortUrl, gid, group, originUrl, favicon, enableStatus} = rowInfo
  originUrl1.value = originUrl
  favicon1.value = favicon
  isGroup.value = group
  tableFullShortUrl.value = fullShortUrl
  tableGid.value = gid
  visitLink.fullShortUrl = fullShortUrl
  visitLink.gid = gid
  chartsInfoRef?.value.isVisible()
  if (!dateList) {
    initStatsFormData()
  } else {
    statsFormData.startDate = dateList?.[0] + ' 00:00:00'
    statsFormData.endDate = dateList?.[1] + ' 23:59:59'
  }
  let res = null
  let tableRes = null
  if (group) {
    res = await API.group.queryGroupStats({...statsFormData, fullShortUrl, gid})
    tableRes = await API.group.queryGroupTable({gid, ...statsFormData})
  } else {
    res = await API.smallLinkPage.queryLinkStats({...statsFormData, fullShortUrl, gid, enableStatus})
    tableRes = await API.smallLinkPage.queryLinkTable({gid, fullShortUrl, ...statsFormData, enableStatus})
  }
  tableInfo.value = tableRes
  chartsInfo.value = res?.data?.data
}

const changeTimeData = async (rowInfo, dateList) => {
  const {fullShortUrl, gid} = rowInfo
  visitLink.fullShortUrl = fullShortUrl
  visitLink.gid = gid
  if (!dateList) {
    initStatsFormData()
  } else {
    statsFormData.startDate = dateList?.[0] + ' 00:00:00'
    statsFormData.endDate = dateList?.[1] + ' 23:59:59'
  }
  let res = null
  let tableRes = null
  if (isGroup.value) {
    res = await API.group.queryGroupStats({...statsFormData, fullShortUrl, gid})
    tableRes = await API.group.queryGroupTable({gid, ...statsFormData})
  } else {
    res = await API.smallLinkPage.queryLinkStats({...statsFormData, fullShortUrl, gid})
    tableRes = await API.smallLinkPage.queryLinkTable({gid, fullShortUrl, ...statsFormData})
  }
  tableInfo.value = tableRes
  chartsInfo.value = res?.data?.data
}
const changeTime = (dateList) => {
  changeTimeData(visitLink, dateList)
}

const changePage = async (page) => {
  const {current, size} = page
  statsFormData.current = current ?? 1
  statsFormData.size = size ?? 10
  let tableRes = null
  // 判断是分组还是单个短链接
  if (isGroup.value) {
    tableRes = await API.group.queryGroupTable({gid: tableGid.value, ...statsFormData})
  } else {
    tableRes = await API.smallLinkPage.queryLinkTable({
      gid: tableGid.value,
      fullShortUrl: tableFullShortUrl.value,
      ...statsFormData
    })
  }
  tableInfo.value = tableRes
}

const transformGroupData = (oldIndex, newIndex) => {
  const formData = editableTabs.value
  const tempData = formData.splice(oldIndex, 1)
  formData.splice(newIndex, 0, tempData[0])
  formData.forEach((item, index) => {
    item.sortOrder = index
  })
  return formData
}

const initSortable = (className) => {
  const table = document.querySelector('.' + className)
  Sortable.create(table, {
    animation: 150, //动画
    onStart: () => {
      console.log("#onStart")
    },
    // 结束拖动事件
    onEnd: async ({to, from, oldIndex, newIndex, clone, pullMode}) => {
      if (newIndex !== oldIndex) {
        if (selectedIndex.value === oldIndex) {
          selectedIndex.value = newIndex
        } else if (
            oldIndex < newIndex &&
            selectedIndex.value > oldIndex &&
            selectedIndex.value <= newIndex
        ) {
          selectedIndex.value = selectedIndex.value - 1
        } else if (
            oldIndex > newIndex &&
            selectedIndex.value < oldIndex &&
            selectedIndex.value >= newIndex
        ) {
          selectedIndex.value = selectedIndex.value + 1
        }
        const res = await API.group.sortGroup(transformGroupData(oldIndex, newIndex))
      }
    }
  })
}

watch(
    () => selectedIndex.value,
    (newValue) => {
      if (newValue !== -1 && newValue !== -2) {
        queryPage()
      }
    }
)

onMounted(() => {
  initSortable('sortOptions')
})
const tableData = ref([])
const pageParams = reactive({
  gid: null,
  current: 1,
  size: 15,
  orderTag: null
})

watch(
    () => pageParams.orderTag,
    (nV) => {
      queryPage()
    }
)
const totalNums = ref(0)

const queryPage = async () => {
  pageParams.gid = editableTabs.value?.[selectedIndex.value]?.gid
  nums.value = editableTabs.value?.[selectedIndex.value]?.shortLinkCount || 0
  const res = await API.smallLinkPage.queryPage(pageParams)
  if (res?.data.success) {
    tableData.value = res.data?.data?.records
    totalNums.value = +res.data?.data?.total
  } else {
    ElMessage.error(res?.data.message)
  }
}

const handleSizeChange = () => {
  !isRecycleBin.value ? queryPage() : queryRecycleBinPage()
}

const handleCurrentChange = () => {
  !isRecycleBin.value ? queryPage() : queryRecycleBinPage()
}

const getGroupInfo = async (fn) => {
  const res = await API.group.queryGroup()
  editableTabs.value = res.data?.data?.reverse()
  fn && fn()
}

getGroupInfo(queryPage)

const updatePage = () => {
  getGroupInfo(queryPage)
}

const isRecycleBin = ref(false)
const recycleBinNums = ref(0)
const queryRecycleBinPage = () => {
  API.smallLinkPage
      .queryRecycleBin({current: pageParams.current, size: pageParams.size})
      .then((res) => {
        tableData.value = res.data?.data?.records
        totalNums.value = +res.data?.data?.total
        recycleBinNums.value = totalNums.value
      })
}

const recycleBin = () => {
  isRecycleBin.value = true
  selectedIndex.value = -1 // -1作为回收站，-2作为选中其他
  pageParams.current = 1
  pageParams.size = 15
  queryRecycleBinPage()
}

const changeSelectIndex = (index) => {
  selectedIndex.value = index
  isRecycleBin.value = false
}

const isAddGroup = ref(false)
const newGroupName = ref()
const addGroupLoading = ref(false)
const showAddGroup = () => {
  newGroupName.value = ''
  isAddGroup.value = true
}

const addGroup = async () => {
  addGroupLoading.value = true
  const res1 = await API.group.addGroup({name: newGroupName.value})
  if (res1?.data.success) {
    ElMessage.success('添加成功')
    getGroupInfo(queryPage)
  } else {
    ElMessage.error(res1?.data.message)
  }
  isAddGroup.value = false
  addGroupLoading.value = false
}

const deleteGroup = async (gid) => {
  const res = await API.group.deleteGroup({gid})
  selectedIndex.value = 0
  if (res.data.success) {
    ElMessage.success('删除成功')
    getGroupInfo(queryPage)
  } else {
    ElMessage.error(res.data.message)
  }
}

const isEditGroup = ref(false)
const editGroupName = ref()
const editGid = ref('')
const editGroupLoading = ref(false)
const showEditGroup = (gid, name) => {
  editGid.value = gid
  editGroupName.value = name
  isEditGroup.value = true
}
const editGroup = async () => {
  editGroupLoading.value = true
  const res = await API.group.editGroup({gid: editGid.value, name: editGroupName.value})
  if (res.data.success) {
    ElMessage.success('Edit Success')
    getGroupInfo(queryPage)
  } else {
    ElMessage.error('Edit Failure')
  }
  isEditGroup.value = false
  editGroupLoading.value = false
}
const isAddSmallLink = ref(false)
const isAddSmallLinks = ref(false)

const addLink = () => {
  isAddSmallLink.value = false
  isAddSmallLinks.value = false
}

const cancelAddLink = () => {
  isAddSmallLink.value = false
  isAddSmallLinks.value = false
}

const getImgUrl = (url) => {
  return url ?? defaultImg
}

const isExpire = (validDate) => {
  if (validDate) {
    const date = new Date(validDate).getTime()
    return new Date().getTime() < date
  }
}

const copyUrl = (url) => {
  let eInput = document.createElement('input')
  eInput.value = url
  document.body.appendChild(eInput)
  eInput.select()
  let copyText = document.execCommand('Copy')
  eInput.style.display = 'none'
  if (copyText) {
    // console.log(eInput.value)
    ElMessage.success('Short Link Copy Success!')
  }
}

const isEditLink = ref(false)
const editLinkRef = ref()
const editData = ref()
const editLink = (data) => {
  editData.value = data
  isEditLink.value = true
}

const coverEditLink = () => {
  isEditLink.value = false
}

const toRecycleBin = (data) => {
  const {gid, fullShortUrl} = data
  API.smallLinkPage
      .toRecycleBin({gid, fullShortUrl})
      .then((res) => {
        if (res?.data?.code !== '0') {
          ElMessage.error(res.data.message)
        } else {
          ElMessage.success('Delete Success')
          getGroupInfo(queryPage)
        }
      })
      .catch((reason) => {
        ElMessage.error('Delete Fail')
      })
}

const recoverLink = (data) => {
  const {gid, fullShortUrl} = data
  API.smallLinkPage
      .recoverLink({gid, fullShortUrl})
      .then((res) => {
        ElMessage.success('Recover Success')
        queryRecycleBinPage()
        // getGroupInfo(queryPage)
        getGroupInfo()
      })
      .catch((reason) => {
        ElMessage.error('Recover Failure')
      })
}

const removeLink = (data) => {
  const {gid, fullShortUrl} = data
  API.smallLinkPage
      .removeLink({gid, fullShortUrl})
      .then((res) => {
        if (res?.data?.code !== '0') {
          ElMessage.error(res.data.message)
        } else {
          ElMessage.success('Delete Success')
          queryRecycleBinPage()
        }
      })
      .catch((reason) => {
        ElMessage.error('Delete Failure')
      })
}

</script>

<!-- CSS Styles -->
<style lang="scss" scoped>
.flex-box {
  display: flex;
  align-items: center;
  padding: 0 10px;
  justify-content: space-between;
}

.hover-box:hover {
  cursor: pointer;
  color: rgba(40, 145, 206, 0.6);
  background-color: #f7f7f7;
  box-shadow: 0px 2px 8px 0px rgba(28, 41, 90, 0.1);
}

.option-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 56px;
  font-size: 15px;
  font-weight: 600;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);

  span {
    font-size: 12px;
    font-weight: 400;
  }
}

.options-box {
  position: relative;
  height: 100%;
  width: 190px;
  border-right: 1px solid rgba(0, 0, 0, 0.1);

  .item-box {
    height: 43px;
    width: 190px;
    font-family:
        PingFangSC-Semibold,
        PingFang SC;
    font-weight: 520;
  }

  .item-box:hover {
    .flex-box {
      .edit {
        display: block;
      }

      .item-length {
        display: none !important;
      }
    }
  }
}

.recycle-bin {
  position: absolute;
  display: flex;
  bottom: 0;
  height: 50px;
  width: 100%;
}

.recycle-box {
  flex: 1;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit {
  display: none;
  margin-left: 5px;
  color: rgb(83, 97, 97);
  font-size: 20px;
}

.edit:hover {
  color: #2991ce;
  cursor: pointer;
}

.zero {
  color: rgb(83, 97, 97) !important;
}

// 提示框样式
.tooltip-base-box {
  width: 600px;
}

.tooltip-base-box .row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.tooltip-base-box .center {
  justify-content: center;
}

.tooltip-base-box .box-item {
  width: 110px;
}

.selectedItem {
  color: #3464e0 !important;
  background-color: #ebeffa !important;
  font-weight: 600 !important;
}

.block:hover {
  .el-icon {
    color: rgb(121, 187, 255) !important;
  }
}

.table-edit {
  font-size: 20px;
  margin-right: 20px;
  color: #3677c2;
  cursor: pointer;
}

.table-edit:hover {
  color: #98cafe;
}

.qr-code {
  margin-right: 20px;
  cursor: pointer;
}

.qr-code:hover {
  opacity: 0.5;
}

.content-box {
  flex: 1;
  padding: 16px;
  background-color: #eef0f5;
  position: relative;

  .table-box {
    background-color: #ffffff;
    height: 100%;

    .buttons-box {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 16px;
    }

    .pagination-block {
      position: absolute;
      bottom: 4%;
      left: 50%;
      transform: translate(-50%, 0);
    }

    .recycle-bin-box {
      height: 64px;
      display: flex;
      align-items: center;
      padding-left: 16px;

      span:nth-child(1) {
        font-size: 20px;
        margin-right: 5px;
      }
    }
  }
}

.over-text {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
}

.table-link-box {
  display: flex;
  align-items: center;

  .name-date {
    display: flex;
    flex-direction: column;
    margin-left: 10px;

    span:nth-child(1) {
      font-size: 15px;
      font-weight: 500;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 1;
    }

    .time {
      display: flex;
      align-items: center;

      span {
        font-size: 12px;
      }

      img {
        margin-left: 10px;
      }

      div {
        border: 1.5px solid rgb(253, 81, 85);
        border-radius: 8px;
        line-height: 20px;
        font-size: 12px;
        transform: scale(0.7);
        color: rgb(253, 81, 85);
        padding: 0 4px;
        background-color: rgba(250, 210, 211);

        span {
          font-weight: bolder;
        }
      }
    }
  }
}

.isExpire {
  .name-date {
    span:nth-child(1) {
      color: rgba(0, 0, 0, 0.3);
    }

    .time {
      div {
        span {
          font-weight: bolder;
          color: red;
        }
      }
    }
  }
}

.table-url-box {
  display: flex;
  flex-direction: column;
  align-items: flex-start;

  span {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 1;
    color: rgba(0, 0, 0, 0.4);
  }
}

.times-box {
  display: flex;
  flex-direction: column;

  .today-box {
    span {
      font-size: 13px;
      font-weight: 600;
      margin-right: 10px;
    }

    span:nth-child(1) {
      font-weight: 400;
      color: rgba(0, 0, 0, 0.4);
    }
  }

  .total-box {
    span {
      font-size: 13px;
      font-weight: 400;
      margin-right: 10px;
    }

    span:nth-child(1) {
      font-weight: 400;
      color: rgba(0, 0, 0, 0.4);
    }
  }
}

.copy-url {
  margin-left: 10px;
}

.demo-tabs > .el-tabs__content {
  font-size: 32px;
  font-weight: 600;
}

.demo-tabs .custom-tabs-label .el-icon {
  vertical-align: middle;
}

.demo-tabs .custom-tabs-label span {
  vertical-align: middle;
  margin-left: 4px;
}
</style>
