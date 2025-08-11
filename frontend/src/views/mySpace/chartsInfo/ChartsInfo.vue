<template>
  <el-dialog v-model="dialogVisible" :title="props.title" width="70%" :before-close="handleClose">
    <template #header>
      <div style="display: flex">
        <img v-if="!isGroup" :src="getImgUrl(props.favicon)" width="25" height="25" alt=""/>
        <div style="display: flex; flex-direction: column; margin-left: 5px">
          <span style="font-size: 25px; line-height: 25px; font-weight: 550">{{
              props.title
            }}</span>
          <span v-if="!isGroup" style="margin-top: 5px; font-size: 15px">{{
              props.originUrl
            }}</span>
        </div>
      </div>
      <span v-if="isGroup" style="margin: 5px 0 0 5px">Total {{ props.nums }} Short Links</span>
    </template>
    <div style="position: absolute; right: 30px; z-index: 999">
      <el-date-picker v-model="dateValue" :clearable="true" type="daterange" range-separator="To"
                      start-placeholder="Start Date"
                      end-placeholder="End Date" value-format="YYYY-MM-DD" :shortcuts="shortcuts" :size="size"/>
    </div>
    <el-tabs v-model="showPane">
      <el-tab-pane name="Access Data" label="Access Data">
        <div class="content-box scroll-box" style="height: calc(100vh - 280px); overflow: scroll">
          <TitleContent class="chart-item" style="width: 800px" title="Access Statistics" @onMounted="initLineChart">
            <template v-slot:titleButton>
              <div>
                <el-button @click="isLine = !isLine">切换为曲线</el-button>
              </div>
            </template>
            <template #content>
              <div class="list-chart">
                <div v-show="isLine" class="top10" style="padding-top: 20px">
                  <div class="key-value" style="margin-top: 10px">
                    <span>Total PV Cnt</span>
                    <span>{{ totalPv }}</span>
                  </div>
                  <div class="key-value" style="margin-top: 10px">
                    <span>Total UV Cnt</span>
                    <span>{{ totalUv }}</span>
                  </div>
                  <div class="key-value" style="margin-top: 10px">
                    <span>Total IP Cnt</span>
                    <span>{{ totalUip }}</span>
                  </div>
                </div>
                <div v-show="isLine" class="lineChart"></div>
                <div v-show="!isLine" style="padding: 20px">
                  <el-table :data="visitsData" border style="width: 100%; height: 210px; overflow: scroll"
                            :header-cell-style="{ background: '#eef1f6', color: '#606266' }">
                    <el-table-column prop="date" label="Date" width="160"/>
                    <el-table-column prop="pv" label="Total PV Count" width="160"/>
                    <el-table-column prop="uv" label="Total UV Count" width="160"/>
                    <el-table-column prop="uip" label="Total IP Count" width="160"/>
                  </el-table>
                </div>
              </div>
            </template>
          </TitleContent>
          <TitleContent class="chart-item" title="24 Hour Distribution" style="width: 800px">
            <template #content>
              <BarChart style="height: 100%; width: 100%" :chartData="{
                xAxis: [
                  0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
                  22, 23
                ],
                value: props.info?.hourStats || new Array(24).fill(0)
              }"></BarChart>
            </template>
          </TitleContent>
          <TitleContent class="chart-item" title="High IP Frequency" style="width: 390px">
            <template #content>
              <KeyValue :dataLists="props.info?.topIpStats" style="height: 100%; width: 100%"></KeyValue>
            </template>
          </TitleContent>
          <TitleContent class="chart-item" title="Weekly Distribution" style="width: 390px">
            <template #content>
              <BarChart style="height: 100%; width: 100%" :chartData="{
                xAxis: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
                value: props.info?.weekdayStats || new Array(7).fill(0)
              }"></BarChart>
            </template>
          </TitleContent>
          <TitleContent class="chart-item" title="OS Info" style="width: 390px">
            <template #content>
              <ProgressLine style="height: 100%; width: 100%" :dataLists="props.info?.osStats"></ProgressLine>
            </template>
          </TitleContent>
          <TitleContent class="chart-item" title="Browser Info" style="width: 390px">
            <template #content>
              <ProgressLine style="height: 100%; width: 100%" :dataLists="props.info?.browserStats"></ProgressLine>
            </template>
          </TitleContent>
          <TitleContent v-if="!isGroup" class="chart-item" title="Visitor Type" style="width: 390px">
            <template #content>
              <ProgressPie style="height: 100%; width: 100%" :labels="['New Visitor', 'Old Visitor']"
                           :data="userTypeList"></ProgressPie>
            </template>
          </TitleContent>
          <TitleContent class="chart-item" title="Network" style="width: 390px">
            <template #content>
              <ProgressPie style="height: 100%; width: 100%" :labels="['WIFI', 'Mobile Web']"
                           :data="netWorkList"></ProgressPie>
            </template>
          </TitleContent>
          <TitleContent class="chart-item" title="Device" style="width: 390px">
            <template #content>
              <ProgressPie style="height: 100%; width: 100%" :labels="['PC', 'Mobile']"
                           :data="deviceList"></ProgressPie>
            </template>
          </TitleContent>
        </div>
      </el-tab-pane>
      <el-tab-pane name="History" label="History">
        <el-table :data="tableInfo?.data?.data?.records" style="width: 100%; height: calc(100vh - 300px)">
          <el-table-column prop="createTime" label="Access Timestamp" width="160"/>
          <el-table-column prop="ip" label="Access IP" width="140"/>
          <el-table-column prop="locale" label="Access Region"></el-table-column>
          <el-table-column prop="device" label="Access Device">
            <template #default="scope">
              <div class="flex-box">
                <img :src="getUrl1(scope?.row?.browser)" width="20" alt=""/>
                <img :src="getUrl2(scope?.row?.os)" width="20" alt=""/>
                <img :src="getUrl3(scope?.row?.device)" width="20" alt=""/>
                <img :src="getUrl4(scope?.row?.network)" width="20" alt=""/>
              </div>
            </template>
          </el-table-column>

          <el-table-column v-if="!isGroup" prop="uvType" label="Visitor Type"/>
        </el-table>
        <div class="pagination-block">
          <el-pagination v-model:current-page="pageParams.current" v-model:page-size="pageParams.size"
                         :page-sizes="[10, 15, 20, 30]" layout="total, sizes, prev, pager, next, jumper"
                         :total="totalNums"
                         @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
        </div>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script setup>
import {reactive, ref, watch} from 'vue'
import TitleContent from './TitleContent.vue'
import * as echarts from 'echarts'
import BarChart from './BarChart.vue'
import KeyValue from './KeyValue.vue'
import ProgressLine from './ProgressLine.vue'
import ProgressPie from './ProgressPie.vue'
import edge from '@/assets/png/edge.png'
import Andriod from '@/assets/png/Andriod.png'
import Chorme from '@/assets/png/Chorme.png'
import firefox from '@/assets/png/firefox.png'
import iOS from '@/assets/png/iOS.png'
import macOS from '@/assets/png/macOS.png'
import other from '@/assets/png/other.png'
import Safair from '@/assets/png/Safair.png'
import Windows from '@/assets/png/Windows.png'
import linux from '@/assets/png/linux.png'
import wifi from '@/assets/png/wifi.png'
import PC from '@/assets/png/pc.png'
import Mobile from '@/assets/png/mobile.png'
import MobileDevices from '@/assets/png/mobile.png.png'
import defaultImg from '@/assets/png/short-link-default-pic.png.png'
import opera from '@/assets/png/opera.png'
import IE from '@/assets/png/IE.png'
import {getLastWeekFormatDate, getTodayFormatDate} from '@/utils/plugins.js'

const shortcuts = [
  {
    text: 'Today',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 0)
      return [start, end]
    }
  },
  {
    text: 'Yesterday',
    value: () => {
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 1)
      return [start, start]
    }
  },
  {
    text: 'Recent 7 Days',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }
  },
  {
    text: 'Recent 1 Monty',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    }
  }
]
const getImgUrl = (url) => {
  return url ?? defaultImg
}
const dailyXAxis = ref()
const showPane = ref('Access Data Records')
const getUrl1 = (img) => {
  if (img) {
    img = img.toLowerCase()
  }
  if (img?.includes('edge')) {
    return edge
  } else if (img?.includes('chrome')) {
    return Chorme
  } else if (img?.includes('fire')) {
    return firefox
  } else if (img?.includes('safari')) {
    return Safair
  } else if (img?.includes('opera')) {
    return opera
  } else if (img?.includes('internet')) {
    return IE
  } else {
    return other
  }
}

const getUrl2 = (img) => {
  if (img) {
    img = img.toLowerCase()
  }
  if (img?.includes('Android')) {
    return Andriod
  } else if (img?.includes('ios')) {
    return iOS
  } else if (img?.includes('mac')) {
    return macOS
  } else if (img?.includes('windows')) {
    return Windows
  } else if (img?.includes('linux')) {
    return linux
  } else {
    return other
  }
}

const getUrl3 = (img) => {
  if (img) {
    img = img.toLowerCase()
  }
  if (img?.includes('pc')) {
    return PC
  } else {
    return Mobile
  }
}

const getUrl4 = (img) => {
  if (img) {
    img = img.toLowerCase()
  }
  if (img?.includes('Mobile')) {
    return MobileDevices
  } else {
    return wifi
  }
}
const dateValue = ref([getLastWeekFormatDate(), getTodayFormatDate()])
const emit = defineEmits(['changeTime', 'changePage'])
watch(
    () => dateValue.value,
    (newValue) => {
      if (!newValue && !dialogVisible.value) {
        return
      }
      emit('changeTime', newValue)
    }
)
const props = defineProps({
  title: {
    type: String,
    default: 'Default Title'
  },
  info: Object,
  tableInfo: Object,
  isGroup: Boolean,
  nums: Number,
  favicon: String,
  originUrl: String
})
const pageParams = reactive({
  current: 1,
  size: 10
})
const totalNums = ref(0)
watch(
    () => props.tableInfo,
    () => {
      totalNums.value = props?.tableInfo?.data?.data?.total
    }
)
watch(
    () => pageParams,
    (newValue) => {
      if (!newValue && !dialogVisible.value) {
        return
      }
      emit('changePage', newValue)
    },
    {
      deep: true
    }
)

const dialogVisible = ref(false)
const handleClose = () => {
  dateValue.value = null
  unVisible()
  showPane.value = 'Access Data'
  dateValue.value = [getLastWeekFormatDate(), getTodayFormatDate()]
  document.querySelector('.scroll-box').scrollTop = 0
}
const isVisible = () => {
  dialogVisible.value = true
}
const unVisible = () => {
  dialogVisible.value = false
}
defineExpose({
  unVisible,
  isVisible
})

const isLine = ref(true)
const initLineChart = () => {
  const lineChartDom = document.querySelector('.lineChart')
  const lineChart = echarts.init(lineChartDom)
  let option = {
    title: {
      show: false,
      text: 'Stacked Line'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['PV Count', 'UV Count', 'IP Count']
    },
    grid: {
      left: '3%',
      right: '9%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dailyXAxis.value
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: 'Page View Count',
        type: 'line',
        data: pvList.value
      },
      {
        name: 'Unique Visitor Count',
        type: 'line',
        data: uvList.value
      },
      {
        name: 'Uip Count',
        type: 'line',
        data: uipList.value
      }
    ]
  }
  lineChart.setOption(option)
}

const totalPv = ref(0)
const pvList = ref([])
const totalUv = ref(0)
const uvList = ref([])
const totalUip = ref(0)
const uipList = ref([])

watch(
    () => props?.info?.daily,
    () => {
      totalPv.value = 0
      totalUv.value = 0
      totalUip.value = 0
      pvList.value = []
      uvList.value = []
      uipList.value = []
      dailyXAxis.value = []
      visitsData.value = props?.info?.daily
      visitsData?.value?.forEach((item) => {
        const {pv, uv, uip, date} = item
        const formDate = date.split('-')[1] + '月' + date.split('-')[2] + '日'
        totalPv.value += pv
        totalUv.value += uv
        totalUip.value += uip
        pvList.value.push(pv)
        uvList.value.push(uv)
        uipList.value.push(uip)
        dailyXAxis.value.push(formDate)
      })
      initLineChart()
    }
)

const visitsData = ref()
const userTypeList = ref([0, 0])
const deviceList = ref([0, 0])
const netWorkList = ref([0, 0])
watch(
    () => props.info?.uvTypeStats,
    () => {
      userTypeList.value = [0, 0]
      props.info?.uvTypeStats?.forEach((item) => {
        if (item.uvType === 'newUser') {
          userTypeList.value[0] = item.cnt
        } else if (item.uvType === 'oldUser') {
          userTypeList.value[1] = item.cnt
        }
      })
    },
    {
      immediate: true
    }
)
watch(
    () => props.info?.deviceStats,
    () => {
      deviceList.value = [0, 0]
      props.info?.deviceStats?.forEach((item) => {
        if (item.device === 'Mobile') {
          deviceList.value[1] = item.cnt
        } else {
          deviceList.value[0] = item.cnt
        }
      })
    },
    {
      immediate: true
    }
)
watch(
    () => props.info?.networkStats,
    () => {
      netWorkList.value = [0, 0]
      props.info?.networkStats?.forEach((item) => {
        if (item.device === 'Mobile') {
          netWorkList.value[1] = item.cnt
        } else {
          netWorkList.value[0] = item.cnt
        }
      })
    },
    {
      immediate: true
    }
)
</script>

<style lang="less" scoped>
.content-box {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  width: 100%;

  .chart-item {
    height: 300px;
    min-width: 300px;
    margin: 10px;
  }
}

.list-chart {
  display: flex;
  justify-content: space-between;

  .top10 {
    padding: 15px 30px;
    width: 400px;
    height: 270px;
    display: flex;
    flex-direction: column;
    overflow-y: auto;

    .top-item {
      display: flex;
      flex-direction: column;
      flex-wrap: wrap;

      div {
        height: 40px;
        display: flex;
        align-items: center;
        margin-right: 30px;
      }
    }

    span:nth-child(1) {
      color: #3464e0;
      font-size: 12px;
      font-weight: 700;
    }

    .key-value {
      display: flex;
      justify-content: space-between;
      width: 150px;
    }
  }
}

.lineChart {
  margin: 10px;
  width: 600px;
  height: 200px;
}

.flex-box {
  display: flex;
  justify-content: space-around;
}

.pagination-block {
  .el-pagination {
    margin-left: 20%;
  }
}
</style>