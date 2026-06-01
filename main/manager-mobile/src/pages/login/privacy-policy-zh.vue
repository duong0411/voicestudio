<route lang="jsonc" type="page">
{
  "layout": "default",
  "style": {
    "navigationStyle": "custom",
    "navigationBarTitleText": "Chính sách bảo mật"
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
      :title="t('login.privacyPolicy')"
      safe-area-inset-top
      left-arrow
      :bordered="false"
      @click-left="goBack"
    >
      <template #left>
        <wd-icon name="arrow-left" size="18" />
      </template>
    </wd-navbar>
    <scroll-view scroll-y class="box-border flex-1 px-[20rpx] py-[30rpx]" :scroll-with-animation="true">
      <view class="rounded-[16rpx] bg-white px-[30rpx] py-[40rpx] shadow-[0_4rpx_20rpx_rgba(0,0,0,0.05)]">
        <view class="mb-[40rpx] text-center">
          <text class="text-[36rpx] text-[#1a1a1a] font-bold">
            {{ t('login.privacyPolicy') }}
          </text>
        </view>
        <view class="mb-[40rpx] text-center">
          <text class="text-[24rpx] text-[#666666]">
            Cập nhật ngày: 21 tháng 05, 2026
          </text>
        </view>
        <view class="mb-[32rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8] font-bold">
            Giới thiệu
          </text>
        </view>
        <view class="mb-[24rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            Chào mừng bạn đến với Dịch vụ Voice AL Studio. Chúng tôi cam kết bảo vệ thông tin cá nhân và quyền riêng tư của bạn. Chính sách này mô tả cách chúng tôi thu thập, sử dụng và bảo vệ dữ liệu của bạn khi bạn sử dụng dịch vụ của chúng tôi.
          </text>
        </view>
        <view class="mb-[24rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            Vui lòng đọc kỹ chính sách bảo mật trước khi sử dụng dịch vụ. Khi bạn bắt đầu sử dụng dịch vụ, điều đó có nghĩa là bạn đồng ý với các điều khoản của chính sách bảo mật này.
          </text>
        </view>
        <view class="mb-[32rpx]">
          <text class="text-[30rpx] text-[#1a1a1a] font-bold">
            1. Thông tin chúng tôi thu thập
          </text>
        </view>
        <view class="mb-[16rpx] ml-[20rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            1.1 Thông tin tài khoản: Tên đăng nhập, mật khẩu mã hóa, số điện thoại hoặc email đăng ký.
          </text>
        </view>
        <view class="mb-[16rpx] ml-[20rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            1.2 Dữ liệu thiết bị: Địa chỉ MAC thiết bị, thông tin phần cứng và hệ điều hành.
          </text>
        </view>
        <view class="mb-[16rpx] ml-[20rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            1.3 Tương tác giọng nói: Dữ liệu giọng nói đầu vào và đầu ra để thực hiện cuộc thoại AI thông minh.
          </text>
        </view>
        <view class="mb-[32rpx]">
          <text class="text-[30rpx] text-[#1a1a1a] font-bold">
            2. Cách chúng tôi sử dụng thông tin
          </text>
        </view>
        <view class="mb-[16rpx] ml-[20rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            Chúng tôi sử dụng thông tin thu thập được để duy trì, vận hành và nâng cao chất lượng dịch vụ AI, quản lý các thiết bị IoT và bảo mật hệ thống.
          </text>
        </view>
        <view class="mb-[32rpx]">
          <text class="text-[30rpx] text-[#1a1a1a] font-bold">
            3. Bảo mật dữ liệu
          </text>
        </view>
        <view class="mb-[16rpx] ml-[20rpx]">
          <text class="text-[28rpx] text-[#333333] leading-[1.8]">
            Dữ liệu của bạn được lưu trữ an toàn trên các máy chủ được cấu hình HTTPS/SSL. Chúng tôi thực hiện các biện pháp kiểm soát truy cập và mã hóa dữ liệu nghiêm ngặt.
          </text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>
