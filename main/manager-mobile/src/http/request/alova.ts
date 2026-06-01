import type { uniappRequestAdapter } from '@alova/adapter-uniapp'
import type { IResponse } from './types'
import type { Language } from '@/store/lang'
import AdapterUniapp from '@alova/adapter-uniapp'
import { createAlova } from 'alova'
import { createServerTokenAuthentication } from 'alova/client'
import VueHook from 'alova/vue'
import { getEnvBaseUrl } from '@/utils'
import { toast } from '@/utils/toast'
import { ContentTypeEnum, ResultEnum, ShowMessage } from './enum'


const langMap: Record<Language, string> = {
  zh_CN: 'zh-CN',
  en: 'en-US',
  zh_TW: 'zh-TW',
  de: 'de',
  vi: 'vi',
  pt_BR: 'pt-BR',
}


const { onAuthRequired, onResponseRefreshToken } = createServerTokenAuthentication<
  typeof VueHook,
  typeof uniappRequestAdapter
>({
  refreshTokenOnError: {
    isExpired: (error) => {
      return error.response?.status === ResultEnum.Unauthorized
    },
    handler: async () => {
      try {
        // await authLogin();
      }
      catch (error) {

        await uni.reLaunch({ url: '/pages/login/index' })
        throw error
      }
    },
  },
})


const alovaInstance = createAlova({
  baseURL: getEnvBaseUrl(),
  ...AdapterUniapp(),
  timeout: 5000,
  statesHook: VueHook,

  beforeRequest: onAuthRequired((method) => {

    const currentBaseUrl = getEnvBaseUrl()
    if (currentBaseUrl !== method.baseURL) {
      method.baseURL = currentBaseUrl
    }


    const currentProtocol = typeof window !== 'undefined' && window.location.protocol
    const requestProtocol = method.baseURL?.split(':')[0]
    const currentLang = langMap[uni.getStorageSync('app_language') as Language || 'zh_CN']
    if (currentProtocol === 'https:' && requestProtocol === 'http') {
      const errorMessage = 'http,'
      throw new Error(errorMessage)
    }


    method.config.headers = {
      'Content-Type': ContentTypeEnum.JSON,
      'Accept': 'application/json, text/plain, */*',
      'Accept-language': currentLang,
      ...method.config.headers,
    }

    const { config } = method
    const ignoreAuth = config.meta?.ignoreAuth
    console.log('ignoreAuth===>', ignoreAuth)


    if (!ignoreAuth) {
      const authInfo = JSON.parse(uni.getStorageSync('token') || '{}')
      if (!authInfo.token) {

        uni.reLaunch({ url: '/pages/login/index' })
        throw new Error('[]：')
      }

      method.config.headers.Authorization = `Bearer ${authInfo.token}`
    }


    if (config.meta?.domain) {
      method.baseURL = config.meta.domain
      console.log('', method.baseURL)
    }
  }),

  responded: onResponseRefreshToken((response, method) => {
    const { config } = method
    const { requestType } = config
    const {
      statusCode,
      data: rawData,
      errMsg,
    } = response as UniNamespace.RequestSuccessCallbackResult

    console.log(response)


    if (requestType === 'upload' || requestType === 'download') {
      return response
    }


    if (statusCode !== 200) {
      const errorMessage = ShowMessage(statusCode) || `HTTP[${statusCode}]`
      console.error('errorMessage===>', errorMessage)
      toast.error(errorMessage)
      throw new Error(`${errorMessage}：${errMsg}`)
    }


    const { code, msg, data } = rawData as IResponse
    if (code !== ResultEnum.Success) {

      if (code === ResultEnum.Unauthorized) {

        uni.removeStorageSync('token')
        uni.reLaunch({ url: '/pages/login/index' })
        throw new Error(`[${code}]：${msg}`)
      }

      if (config.meta?.isExposeError) {
        return Promise.reject(msg)
      }

      if (config.meta?.toast !== false) {
        toast.warning(msg)
      }
      throw new Error(`[${code}]：${msg}`)
    }

    return data
  }),
})

export const http = alovaInstance
