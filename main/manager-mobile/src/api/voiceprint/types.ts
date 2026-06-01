
export interface VoicePrint {
  id: string
  audioId: string
  sourceName: string
  introduce: string
  createDate: string
}


export interface ChatHistory {
  content: string
  audioId: string
}


export interface CreateSpeakerData {
  agentId: string
  audioId: string
  sourceName: string
  introduce: string
}


export interface ApiResponse<T = any> {
  code: number
  msg: string
  data: T
}
