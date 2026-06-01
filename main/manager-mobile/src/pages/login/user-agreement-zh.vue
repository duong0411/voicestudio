<route lang="jsonc" type="page">
{
  "layout": "default",
  "style": {
    "navigationStyle": "custom",
    "navigationBarTitleText": "Thỏa thuận người dùng"
  }
}
</route>

<script lang="ts" setup>
import { t } from '@/i18n'

function goBack() {
  uni.navigateBack()
}
</script>

<template>
  <view class="h-screen flex flex-col bg-[#f5f8fd]">
    <wd-navbar
      :title="t('login.userAgreement')"
      safe-area-inset-top
      left-arrow
      :bordered="false"
      @click-left="goBack"
    >
      <template #left>
        <wd-icon name="arrow-left" size="18" />
      </template>
    </wd-navbar>
    <scroll-view scroll-y class="box-border flex-1 px-[30rpx] py-[30rpx]">
      <view class="rounded-[16rpx] bg-white px-[30rpx] py-[40rpx] shadow-[0_4rpx_20rpx_rgba(0,0,0,0.05)]">
        <view class="mb-[40rpx] text-center">
          <text class="text-[36rpx] text-[#1a1a1a] font-bold">
            {{ t('login.userAgreement') }}
          </text>
        </view>
        <view class="mb-[40rpx] text-center">
          <text class="text-[24rpx] text-[#666666]">
            Cập nhật ngày: 21 tháng 05, 2026
          </text>
        </view>
        <view class="mb-[32rpx]">
          <text class="text-[28rpx] text-[#333333] font-semibold">
            Điều khoản Thỏa thuận
          </text>
        </view>
        <view class="mb-[24rpx] ml-[20rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            Chào mừng bạn đến với Dịch vụ Voice AL Studio. Thỏa thuận này quy định các điều kiện và điều khoản ràng buộc pháp lý khi bạn sử dụng dịch vụ của chúng tôi cho mục đích quản lý thiết bị, cấu hình tác nhân AI và đàm thoại thông minh.
          </text>
        </view>
        <view class="mb-[24rpx] ml-[20rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            Bằng việc đăng ký tài khoản, đăng nhập hoặc sử dụng dịch vụ dưới bất kỳ hình thức nào, điều này đồng nghĩa với việc bạn đã đọc, hiểu và chấp thuận toàn bộ nội dung của thỏa thuận này.
          </text>
        </view>
        <view class="mb-[32rpx]">
          <text class="text-[28rpx] text-[#333333] font-semibold">
            1. Mô tả Dịch vụ
          </text>
        </view>
        <view class="mb-[24rpx] ml-[20rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            Hệ thống cung cấp giải pháp kết nối, cấu hình phần cứng IoT (ví dụ ESP32) với các mô hình ngôn ngữ lớn (LLM), nhận dạng giọng nói (ASR), tổng hợp giọng nói (TTS) để phục vụ cho các tương tác đàm thoại thời gian thực.
          </text>
        </view>
        <view class="mb-[32rpx]">
          <text class="text-[28rpx] text-[#333333] font-semibold">
            2. Tài khoản Người dùng
          </text>
        </view>
        <view class="mb-[24rpx] ml-[20rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            Bạn chịu trách nhiệm bảo vệ thông tin tài khoản và mật khẩu của mình. Mọi tổn thất do rò rỉ thông tin từ phía người dùng sẽ do bạn tự chịu trách nhiệm.
          </text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<style lang="scss" scoped>
</style>
