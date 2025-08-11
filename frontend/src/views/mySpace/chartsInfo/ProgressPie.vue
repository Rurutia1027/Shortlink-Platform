<template>

</template>

<script setup>
const props = defineProps({
  labels: {
    type: Array,
    // eslint-disable-next-line vue/require-valid-default-prop
    default: ['dataset1', 'dataset2']
  },
  data: {
    type: Array,
    default: [10, 0]
  }
})

const data1 = props.data[0]
const data2 = props.data[1]
const data1Precentage = ref(0)
const data2Precentage = ref(0)

watch(
    () => props.data,
    () => {
      const data1 = props.data[0]
      const data2 = props.data[1]
      data1Precentage.value = (function () {
        if (data1 === 0) {
          return 0
        } else {
          return ((data1 / (data1 + data2)) * 100).toFixed(0)
        }
      })()
      data2Precentage.value = (function () {
        if (data2 === 0) {
          return 0
        } else {
          return ((data2 / (data1 + data2)) * 100).toFixed(0)
        }
      })()
    }
)
</script>

<style lang="scss" scoped>
.main-box {
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: space-around;
}

.flex-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 14px;
  font-weight: 600;

  .percentage-value {
    margin-bottom: 5px;
  }
}
</style>