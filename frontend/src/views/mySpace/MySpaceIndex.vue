<template>

</template>
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
