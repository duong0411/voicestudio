/// <reference types="vite/client" />
/// <reference types="vite-svg-loader" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'

  const component: DefineComponent<{}, {}, any>
  export default component
}

interface ImportMetaEnv {
  
  readonly VITE_APP_TITLE: string
  
  readonly VITE_SERVER_PORT: string
  
  readonly VITE_SERVER_BASEURL: string
  
  readonly VITE_APP_PROXY: 'true' | 'false'
  
  readonly VITE_APP_PROXY_PREFIX: string
  
  readonly VITE_UPLOAD_BASEURL: string
  
  readonly VITE_DELETE_CONSOLE: string

}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

declare const __VITE_APP_PROXY__: 'true' | 'false'
declare const __UNI_PLATFORM__: 'app' | 'h5' | 'mp-alipay' | 'mp-baidu' | 'mp-kuaishou' | 'mp-lark' | 'mp-qq' | 'mp-tiktok' | 'mp-weixin' | 'mp-xiaochengxu'
