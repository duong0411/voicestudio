import Vue from 'vue';
import VueI18n from 'vue-i18n';
import en from './en';
import vi from './vi';

Vue.use(VueI18n);

export const SUPPORTED_LOCALES = ['en', 'vi'];

export const normalizeLocale = (lang) => {
  if (SUPPORTED_LOCALES.includes(lang)) {
    return lang;
  }
  return 'vi';
};

const getDefaultLanguage = () => {
  const savedLang = localStorage.getItem('userLanguage');
  if (savedLang) {
    return normalizeLocale(savedLang);
  }
  const browserLang = navigator.language || navigator.userLanguage;
  if (browserLang.indexOf('vi') === 0) {
    return 'vi';
  }
  if (browserLang.indexOf('en') === 0) {
    return 'en';
  }
  return 'vi';
};

const i18n = new VueI18n({
  locale: getDefaultLanguage(),
  fallbackLocale: 'en',
  messages: {
    en,
    vi,
  },
});

export default i18n;

export const changeLanguage = (lang) => {
  const locale = normalizeLocale(lang);
  i18n.locale = locale;
  localStorage.setItem('userLanguage', locale);
  Vue.prototype.$eventBus.$emit('languageChanged', locale);
};
