import { getEnvBaseUrl } from './index'
import { toast } from './toast'




export const uploadFileUrl = {
  
  get USER_AVATAR() {
    return `${getEnvBaseUrl()}/user/avatar`
  },
}


export function useFileUpload<T = string>(url: string, filePath: string, formData: Record<string, any> = {}, options: Omit<UploadOptions, 'sourceType' | 'sizeType' | 'count'> = {}) {
  return useUpload<T>(
    url,
    formData,
    {
      ...options,
      sourceType: ['album'],
      sizeType: ['original'],
    },
    filePath,
  )
}

export interface UploadOptions {
  
  count?: number
  
  sizeType?: Array<'original' | 'compressed'>
  
  sourceType?: Array<'album' | 'camera'>
  
  maxSize?: number //
  
  onProgress?: (progress: number) => void
  
  onSuccess?: (res: Record<string, any>) => void
  
  onError?: (err: Error | UniApp.GeneralCallbackResult) => void
  
  onComplete?: () => void
}


export function useUpload<T = string>(url: string, formData: Record<string, any> = {}, options: UploadOptions = {},
  
  directFilePath?: string) {
  
  const loading = ref(false)
  
  const error = ref(false)
  
  const data = ref<T>()
  
  const progress = ref(0)

  
  const {
    
    count = 1,
    
    sizeType = ['original', 'compressed'],
    
    sourceType = ['album', 'camera'],
    
    maxSize = 10,
    
    onProgress,
    
    onSuccess,
    
    onError,
    
    onComplete,
  } = options

  
  const checkFileSize = (size: number) => {
    const sizeInMB = size / 1024 / 1024
    if (sizeInMB > maxSize) {
      toast.warning(`${maxSize}MB`)
      return false
    }
    return true
  }
  
  const run = () => {
    if (directFilePath) {

      loading.value = true
      progress.value = 0
      uploadFile<T>({
        url,
        tempFilePath: directFilePath,
        formData,
        data,
        error,
        loading,
        progress,
        onProgress,
        onSuccess,
        onError,
        onComplete,
      })
      return
    }

    // #ifdef MP-WEIXIN

    uni.chooseMedia({
      count,
      mediaType: ['image'],
      sourceType,
      success: (res) => {
        const file = res.tempFiles[0]

        if (!checkFileSize(file.size))
          return


        loading.value = true
        progress.value = 0
        uploadFile<T>({
          url,
          tempFilePath: file.tempFilePath,
          formData,
          data,
          error,
          loading,
          progress,
          onProgress,
          onSuccess,
          onError,
          onComplete,
        })
      },
      fail: (err) => {
        console.error(':', err)
        error.value = true
        onError?.(err)
      },
    })
    // #endif

    // #ifndef MP-WEIXIN

    uni.chooseImage({
      count,
      sizeType,
      sourceType,
      success: (res) => {
        console.log(':', res)


        loading.value = true
        progress.value = 0
        uploadFile<T>({
          url,
          tempFilePath: res.tempFilePaths[0],
          formData,
          data,
          error,
          loading,
          progress,
          onProgress,
          onSuccess,
          onError,
          onComplete,
        })
      },
      fail: (err) => {
        console.error(':', err)
        error.value = true
        onError?.(err)
      },
    })
    // #endif
  }

  return { loading, error, data, progress, run }
}


interface UploadFileOptions<T> {
  
  url: string
  
  tempFilePath: string
  
  formData: Record<string, any>
  
  data: Ref<T | undefined>
  
  error: Ref<boolean>
  
  loading: Ref<boolean>
  
  progress: Ref<number>
  
  onProgress?: (progress: number) => void
  
  onSuccess?: (res: Record<string, any>) => void
  
  onError?: (err: Error | UniApp.GeneralCallbackResult) => void
  
  onComplete?: () => void
}


function uploadFile<T>({
  url,
  tempFilePath,
  formData,
  data,
  error,
  loading,
  progress,
  onProgress,
  onSuccess,
  onError,
  onComplete,
}: UploadFileOptions<T>) {
  try {

    const uploadTask = uni.uploadFile({
      url,
      filePath: tempFilePath,
      name: 'file',
      formData,
      header: {

        // #ifndef H5
        'Content-Type': 'multipart/form-data',
        // #endif
      },

      success: (uploadFileRes) => {
        console.log(':', uploadFileRes)
        try {

          const { data: _data } = JSON.parse(uploadFileRes.data)

          data.value = _data as T
          onSuccess?.(_data)
        }
        catch (err) {

          console.error(':', err)
          error.value = true
          onError?.(new Error(''))
        }
      },
      fail: (err) => {

        console.error(':', err)
        error.value = true
        onError?.(err)
      },
      complete: () => {

        loading.value = false
        onComplete?.()
      },
    })


    uploadTask.onProgressUpdate((res) => {
      progress.value = res.progress
      onProgress?.(res.progress)
    })
  }
  catch (err) {

    console.error(':', err)
    error.value = true
    loading.value = false
    onError?.(new Error(''))
  }
}
