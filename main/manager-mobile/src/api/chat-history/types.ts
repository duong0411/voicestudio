
export interface ChatSession {
  sessionId: string
  createdAt: string
  chatCount: number
  title: string
}


export interface ChatSessionsResponse {
  total: number
  list: ChatSession[]
}


export interface ChatMessage {
  createdAt: string
  chatType: 1 | 2 | 3
  content: string
  audioId: string | null
  macAddress: string
}


export interface UserMessageContent {
  speaker: string
  content: string
}


export interface GetSessionsParams {
  page: number
  limit: number
}


export interface AudioResponse {
  data: string
}
