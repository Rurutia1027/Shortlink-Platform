<script setup>
defineProps({
  text: {
    type: String,
    default: 'No data for now, please press create short link button'
  }
})
</script>

<template>
  <div class="empty">
    <img src="@/assets/png/nodata.png" alt="" class="empty_img" />
    <p>{{ text }}</p>
    <slot></slot>
  </div>
</template>

<style lang="scss" scoped>
.empty {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #8591a9;
  height: 440px;

  &_img {
    width: 200px;
    margin-bottom: 11px;
  }
}
</style>
