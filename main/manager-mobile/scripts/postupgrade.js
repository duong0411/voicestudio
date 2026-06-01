



const { exec } = require('node:child_process')


const dependencies = [
  '@dcloudio/uni-app-harmony',

  '@dcloudio/uni-mp-alipay',
  '@dcloudio/uni-mp-baidu',
  '@dcloudio/uni-mp-jd',
  '@dcloudio/uni-mp-kuaishou',
  '@dcloudio/uni-mp-lark',
  '@dcloudio/uni-mp-qq',
  '@dcloudio/uni-mp-toutiao',
  '@dcloudio/uni-mp-xhs',
  '@dcloudio/uni-quickapp-webview',

  'vue-i18n',
]


exec(`pnpm un ${dependencies.join(' ')}`, (error, stdout, stderr) => {
  if (error) {

    console.error(`: ${error}`)
    return
  }

  console.log(`stdout: ${stdout}`)

  console.error(`stderr: ${stderr}`)
})
