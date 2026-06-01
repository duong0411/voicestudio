import { ref } from 'vue'
import { useLangStore } from '@/store/lang'
import type { Language } from '@/store/lang'


import zh_CN from './zh_CN'
import en from './en'
import zh_TW from './zh_TW'
import de from './de'
import vi from './vi'
import pt_BR from './pt_BR'


const messages = {
  zh_CN: zh_CN,
  en,
  zh_TW: zh_TW,
  de,
  vi,
  pt_BR: pt_BR,
}


const currentLang = ref<Language>('zh_CN')


export function initI18n() {
  const langStore = useLangStore()
  currentLang.value = langStore.currentLang
}


export function changeLanguage(lang: Language) {
  currentLang.value = lang
  const langStore = useLangStore()
  langStore.changeLang(lang)
}


export function t(key: string, params?: Record<string, string | number>): string {
  const langMessages = messages[currentLang.value]


  if (langMessages && typeof langMessages === 'object' && key in langMessages) {
    let value = langMessages[key]
    if (typeof value === 'string') {

      if (params) {
        let result = value
        Object.entries(params).forEach(([paramKey, paramValue]) => {
          const regex = new RegExp(`\{${paramKey}\}`, 'g')
          result = result.replace(regex, String(paramValue))
        })
        return result
      }
      return value
    }
    return key
  }

  return key
}


export function getCurrentLanguage(): Language {
  return currentLang.value
}


export function getSupportedLanguages(): { code: Language, name: string }[] {
  return [
    { code: 'zh_CN', name: '' },
    { code: 'en', name: 'English' },
    { code: 'zh_TW', name: '' },
    { code: 'de', name: 'Deutsch' },
    { code: 'vi', name: 'Tiếng Việt' },
    { code: 'pt_BR', name: 'Português (Brasil)' },
  ]
}