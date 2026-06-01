<route lang="jsonc" type="page">
{
  "layout": "default",
  "style": {
    "navigationStyle": "custom",
    "navigationBarTitleText": ""
  }
}
</route>

<script lang="ts" setup>
import type { ChatMessage, UserMessageContent } from '@/api/chat-history/types'
import { onLoad, onUnload } from '@dcloudio/uni-app'
import { computed, ref } from 'vue'
import { getAudioId, getChatHistory } from '@/api/chat-history/chat-history'
import { t } from '@/i18n'
import { debounce, getEnvBaseUrl } from '@/utils'
import { toast } from '@/utils/toast'

defineOptions({
  name: 'ChatDetail',
})


let safeAreaInsets: any
let systemInfo: any

// #ifdef MP-WEIXIN
systemInfo = uni.getWindowInfo()
safeAreaInsets = systemInfo.safeArea
  ? {
      top: systemInfo.safeArea.top,
      right: systemInfo.windowWidth - systemInfo.safeArea.right,
      bottom: systemInfo.windowHeight - systemInfo.safeArea.bottom,
      left: systemInfo.safeArea.left,
    }
  : null
// #endif

// #ifndef MP-WEIXIN
systemInfo = uni.getSystemInfoSync()
safeAreaInsets = systemInfo.safeAreaInsets
// #endif


const sessionId = ref('')
const agentId = ref('')


const currentAgent = computed(() => {
  return {
    id: agentId.value,
    agentName: t('chatHistory.assistantName'),
  }
})


const messageList = ref<ChatMessage[]>([])
const loading = ref(false)


const audioContext = ref<UniApp.InnerAudioContext | null>(null)
const playingAudioId = ref<string | null>(null)
const expandedToolResults = ref({})


function goBack() {
  uni.navigateBack()
}


async function loadChatHistory() {
  if (!sessionId.value || !agentId.value) {
    console.error('')
    return
  }

  try {
    loading.value = true
    const response = await getChatHistory(agentId.value, sessionId.value)
    messageList.value = response
  }
  catch (error) {
    console.error(':', error)
    toast.error(t('chatHistory.loadFailed'))
  }
  finally {
    loading.value = false
  }
}


function parseUserMessage(content: string): UserMessageContent | null {
  try {
    return JSON.parse(content)
  }
  catch {
    return null
  }
}


function getMessageContent(message: ChatMessage): string {
  if (message.chatType === 1) {

    const parsed = parseUserMessage(message.content)
    return parsed ? parsed.content : message.content
  }
  else {

    return message.content
  }
}


function getSpeakerName(message: ChatMessage): string {
  if (message.chatType === 1) {
    const parsed = parseUserMessage(message.content)
    return parsed ? parsed.speaker : t('chatHistory.userName')
  }
  else {
    return currentAgent.value?.agentName || t('chatHistory.aiAssistantName')
  }
}


function formatTime(timeStr: string) {
  if (!timeStr)
    return t('chatHistory.unknownTime')


  const date = new Date(timeStr.replace(' ', 'T'))
  const now = new Date()


  if (Number.isNaN(date.getTime())) {
    return timeStr
  }

  const diff = now.getTime() - date.getTime()


  if (diff < 60000)
    return t('chatHistory.justNow')


  if (diff < 3600000)
    return t('chatHistory.minutesAgo', { minutes: Math.floor(diff / 60000) })


  if (diff < 86400000)
    return t('chatHistory.hoursAgo', { hours: Math.floor(diff / 3600000) })


  if (diff < 604800000) {
    const days = Math.floor(diff / 86400000)
    return t('chatHistory.daysAgo', { days })
  }


  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const currentYear = now.getFullYear()


  if (year === currentYear) {
    return `${month}-${day}`
  }

  return `${year}-${month}-${day}`
}


const playAudio = debounce(async (audioId: string) => {
  if (!audioId) {
    toast.error(t('chatHistory.invalidAudioId'))
    return
  }

  try {

    if (audioContext.value) {
      audioContext.value.stop()
    }

    if (playingAudioId.value === audioId) {
      playingAudioId.value = null
      return
    }


    const downloadId = await getAudioId(audioId)


    const baseUrl = getEnvBaseUrl()
    const audioUrl = `${baseUrl}/agent/play/${downloadId}`


    if (!audioContext.value) {
      audioContext.value = uni.createInnerAudioContext()
    }
    audioContext.value.src = audioUrl


    playingAudioId.value = audioId


    audioContext.value.onEnded(() => {
      playingAudioId.value = null
      if (audioContext.value) {
        audioContext.value.destroy()
        audioContext.value = null
      }
    })


    audioContext.value.onError((error) => {
      console.error(':', error)
      toast.error(t('chatHistory.audioPlayFailed'))
      playingAudioId.value = null
      if (audioContext.value) {
        audioContext.value.destroy()
        audioContext.value = null
      }
    })


    audioContext.value.play()
  }
  catch (error) {
    console.error('Playing audio failed:', error)
    toast.error(t('chatHistory.playAudioFailed'))
    playingAudioId.value = null
  }
}, 400)

function extractContentFromString(content: string) {
  if (!content || content.trim() === '') {
    return content
  }


  try {
    const jsonObj = JSON.parse(content)


    if (Array.isArray(jsonObj)) {
      return jsonObj
    }


    if (jsonObj && typeof jsonObj === 'object' && jsonObj.content) {
      return jsonObj.content
    }
  }
  catch (e) {

  }


  return content
}

function toggleToolResult(messageIndex, itemIndex) {
  const key = `${messageIndex}-${itemIndex}`
  expandedToolResults.value[key] = !expandedToolResults.value[key]
}

function isToolResultCollapsed(messageIndex, itemIndex) {
  const key = `${messageIndex}-${itemIndex}`

  return !expandedToolResults.value[key]
}

function getFirstLineText(text: string) {
  if (!text) {
    return ''
  }
  const firstLine = text.split('\n')[0]
  return firstLine.length < text.length ? `${firstLine}...` : text
}

onLoad((options) => {
  if (options?.sessionId && options?.agentId) {
    sessionId.value = options.sessionId
    agentId.value = options.agentId
  }
  else {
    console.error('')
    toast.error(t('chatHistory.parameterError'))
  }
})

onShow(() => {
  loadChatHistory()
})


onUnload(() => {
  if (audioContext.value) {
    audioContext.value.stop()
    audioContext.value.destroy()
    audioContext.value = null
  }
})
</script>

<template>
  <view class="h-screen flex flex-col bg-[#f5f7fb]">
    
    <view class="w-full bg-white" :style="{ height: `${safeAreaInsets?.top}px` }" />

    
    <wd-navbar :title="t('chatHistory.pageTitle')">
      <template #left>
        <wd-icon name="arrow-left" size="18" @click="goBack" />
      </template>
    </wd-navbar>

    
    <scroll-view
      scroll-y
      :style="{ height: `calc(100vh - ${safeAreaInsets?.top || 0}px - 120rpx)` }"
      class="box-border flex-1 bg-[#f5f7fb] p-[20rpx]"
      :scroll-into-view="`message-${messageList.length - 1}`"
    >
      <view v-if="loading" class="flex flex-col items-center justify-center gap-[20rpx] p-[100rpx_0]">
        <wd-loading />
        <text class="text-[28rpx] text-[#65686f]">
          {{ t('chatHistory.loading') }}
        </text>
      </view>

      <view v-else class="flex flex-col gap-[20rpx]">
        <view
          v-for="(message, index) in messageList"
          :id="`message-${index}`"
          :key="index"
          class="w-full flex"
          :class="{
            'justify-end': message.chatType === 1,
            'justify-start': message.chatType === 2,
          }"
        >
          <view
            class="max-w-[80%] flex flex-col gap-[8rpx]"
            :class="{
              'items-end': message.chatType === 1,
              'items-start': message.chatType === 2,
              'tool-message': message.chatType === 3,
            }"
          >
            
            <view
              class="shadow-message break-words rounded-[20rpx] p-[24rpx] leading-[1.4]"
              :class="{
                'bg-[#336cff] text-white': message.chatType === 1,
                'bg-white text-[#232338] border border-[#eeeeee]': [2, 3].includes(message.chatType),
              }"
            >
              <template v-if="Array.isArray(extractContentFromString(message.content))">
                <div class="content-wrapper">
                  <div v-for="(item, idx) in extractContentFromString(message.content)" :key="idx">
                    <div v-if="item.type === 'text'" class="text-content">
                      {{ item.text }}
                    </div>
                    <div v-else-if="item.type === 'tool'" class="tool-call-text">
                      {{ item.text }}
                    </div>
                    <div v-else-if="item.type === 'tool_result'" class="tool-call-text">
                      <div v-if="item.text && item.text.length > 80" class="tool-result-wrapper">
                        <div v-if="isToolResultCollapsed(index, idx)" class="tool-result-collapsed">
                          {{ getFirstLineText(item.text) }}
                        </div>
                        <div v-else class="tool-result-expanded">
                          {{ item.text }}
                        </div>
                        <span class="tool-toggle-btn" @click="toggleToolResult(index, idx)">
                          <wd-icon :name="isToolResultCollapsed(index, idx) ? 'arrow-down' : 'arrow-up'" size="12" />
                        </span>
                      </div>
                      <div v-else>
                        {{ item.text }}
                      </div>
                    </div>
                  </div>
                </div>
              </template>
              
              <view v-else class="flex items-center gap-[12rpx]">
                
                <view
                  v-if="message.audioId"
                  class="flex-shrink-0 cursor-pointer transition-transform duration-200 active:scale-90"
                  :class="{
                    'text-white animate-pulse-audio': message.chatType === 1 && playingAudioId === message.audioId,
                    'text-[#ffd700]': message.chatType === 1 && playingAudioId === message.audioId && playingAudioId,
                    'text-[#336cff] animate-pulse-audio': message.chatType === 2 && playingAudioId === message.audioId,
                    'text-[#ff6b35]': message.chatType === 2 && playingAudioId === message.audioId && playingAudioId,
                    'text-white': message.chatType === 1 && playingAudioId !== message.audioId,
                    'text-[#336cff]': message.chatType === 2 && playingAudioId !== message.audioId,
                  }"
                  @click="playAudio(message.audioId)"
                >
                  <wd-icon
                    :name="playingAudioId === message.audioId ? 'pause-circle-filled' : 'play-circle-filled'"
                    size="20"
                  />
                </view>

                
                <view class="min-w-0 flex-1">
                  
                  <text class="block text-[28rpx]">
                    {{ getMessageContent(message) }}
                  </text>
                </view>
              </view>
            </view>

            
            <text
              class="mx-[12rpx] text-[22rpx] text-[#9d9ea3]"
              :class="{
                'text-right': message.chatType === 1,
                'text-left': message.chatType === 2,
              }"
            >
              {{ formatTime(message.createdAt) }}
            </text>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<style>

.shadow-message {
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
}

@keyframes pulse-audio {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
}

.animate-pulse-audio {
  animation: pulse-audio 1.5s infinite;
}

.text-content {
  display: block;
  margin-bottom: 8rpx;
}

.tool-call-text {
  color: #1890ff;
  font-family: 'Courier New', monospace;
  font-weight: 500;
  font-size: 24rpx;
  display: block;
  margin-top: 8rpx;
}

.user-message .tool-call-text {
  color: #e6f7ff;
}

.tool-message .message-content {
  background-color: #f0f0f0;
}

.tool-result-wrapper {
  position: relative;
  padding-right: 40rpx;
}

.tool-result-collapsed {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tool-toggle-btn {
  position: absolute;
  right: 0;
  top: 0;
  cursor: pointer;
  color: #1890ff;
  font-size: 24rpx;
}
</style>
