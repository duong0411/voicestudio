import 'element-ui/lib/theme-chalk/index.css';
import 'normalize.css/normalize.css'; // A modern alternative to CSS resets
import Vue from 'vue';
import ElementUI from 'element-ui';
import ElementLocale from 'element-ui/lib/locale';
import elementEnLocale from 'element-ui/lib/locale/lang/en';
import App from './App.vue';
import router from './router';
import store from './store';
import i18n from './i18n';
import { installDbLabelLocale } from './utils/dbLabelLocale';
import './styles/global.scss';
import { register as registerServiceWorker } from './registerServiceWorker';
import featureManager from './utils/featureManager';
window.onerror = function(msg, url, line, col, error) {
  const div = document.createElement('div');
  div.style.position = 'fixed';
  div.style.top = '0';
  div.style.left = '0';
  div.style.width = '100vw';
  div.style.height = '100vh';
  div.style.backgroundColor = 'rgba(255, 0, 0, 0.9)';
  div.style.color = 'white';
  div.style.zIndex = '99999';
  div.style.padding = '20px';
  div.style.overflow = 'auto';
  div.style.fontFamily = 'monospace';
  div.innerHTML = `<h1>Global JS Error</h1><p><b>Message:</b> ${msg}</p><p><b>URL:</b> ${url}</p><p><b>Line:</b> ${line}:${col}</p><pre>${error ? error.stack : ''}</pre>`;
  document.body.appendChild(div);
  return false;
};

Vue.config.errorHandler = function(err, vm, info) {
  console.error(err);
  const div = document.createElement('div');
  div.style.position = 'fixed';
  div.style.top = '0';
  div.style.left = '0';
  div.style.width = '100vw';
  div.style.height = '100vh';
  div.style.backgroundColor = 'rgba(255, 0, 0, 0.9)';
  div.style.color = 'white';
  div.style.zIndex = '99999';
  div.style.padding = '20px';
  div.style.overflow = 'auto';
  div.style.fontFamily = 'monospace';
  div.innerHTML = `<h1>Vue Error (${info})</h1><p><b>Message:</b> ${err.message}</p><pre>${err.stack}</pre>`;
  document.body.appendChild(div);
};

Vue.prototype.$eventBus = new Vue();

ElementLocale.use(elementEnLocale);
Vue.use(ElementUI);

Vue.config.productionTip = false;

installDbLabelLocale(Vue, i18n);


registerServiceWorker();


new Vue({
  router,
  store,
  i18n,
  render: function (h) { return h(App) }
}).$mount('#app')
