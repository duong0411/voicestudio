

declare global {
  interface IResData<T> {
    code: number
    msg: string
    data: T
  }


  interface IUniUploadFileOptions {
    file?: File
    files?: UniApp.UploadFileOptionFiles[]
    filePath?: string
    name?: string
    formData?: any
  }

  interface IUserInfo {
    nickname?: string
    avatar?: string
    
    openid?: string
    token?: string
  }
}

export {}
