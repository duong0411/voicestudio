<template>
  <div class="val-auth-page" @keyup.enter="login">
    <section class="val-auth-hero">
      <div class="val-auth-hero-inner">
        <BrandLogo size="lg" />
        <p class="val-auth-tagline">{{ $t('brand.tagline') }}</p>
        <div class="val-auth-features">
          <span v-for="(feat, i) in featureList" :key="i" class="val-feature-pill">{{ feat }}</span>
        </div>
        <div class="val-waveform" aria-hidden="true">
          <span v-for="n in 12" :key="n" class="val-wave-bar" />
        </div>
      </div>
    </section>

    <section class="val-auth-panel">
      <div class="val-auth-card">
        <div class="val-auth-card-header">
          <div class="val-auth-heading">
            <BrandLogo iconOnly size="sm" />
            <h1 class="val-auth-title">{{ $t('login.title') }}</h1>
          </div>
          <el-dropdown
            trigger="click"
            class="val-lang-switch"
            @visible-change="handleLanguageDropdownVisibleChange"
          >
            <span class="val-lang-btn">
              {{ currentLanguageText }}
              <i class="el-icon-arrow-down" :class="{ 'rotate-down': languageDropdownVisible }" />
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="changeLanguage('vi')">{{ $t('language.vi') }}</el-dropdown-item>
              <el-dropdown-item @click.native="changeLanguage('en')">{{ $t('language.en') }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

        <div class="val-auth-form-wrapper">
          <template v-if="!isMobileLogin">
            <div class="val-input-box">
              <i class="el-icon-user" />
              <el-input v-model="form.username" :placeholder="$t('login.usernamePlaceholder')" />
            </div>
          </template>
          <template v-else>
            <div class="val-input-box" style="padding-right: 0">
              <el-select v-model="form.areaCode" style="width: 110px; margin-right: 8px">
                <el-option
                  v-for="item in mobileAreaList"
                  :key="item.key"
                  :label="`${item.name} (${item.key})`"
                  :value="item.key"
                />
              </el-select>
              <el-input v-model="form.mobile" :placeholder="$t('login.mobilePlaceholder')" />
            </div>
          </template>

          <div class="val-input-box">
            <i class="el-icon-lock" />
            <el-input
              v-model="form.password"
              :placeholder="$t('login.passwordPlaceholder')"
              type="password"
              show-password
            />
          </div>

          <div class="val-auth-links">
            <span class="link" @click="goToForgetPassword">{{ $t('login.forgetPassword') }}</span>
          </div>

          <div class="val-primary-btn" @click="login">{{ $t('login.login') }}</div>

          <!-- Google OAuth Sign In -->
          <div v-if="googleClientId" class="val-oauth-divider">
            <span class="val-divider-line"></span>
            <span class="val-divider-text">{{ $t('login.orContinueWith') }}</span>
            <span class="val-divider-line"></span>
          </div>
          <div v-if="googleClientId" class="val-oauth-container">
            <div id="google-btn-container" class="google-btn-container"></div>
          </div>

          <div class="val-register-block">
            <p class="val-no-account">{{ $t('login.noAccount') }}</p>
            <button type="button" class="val-secondary-btn" @click="goToRegister">
              {{ $t('login.registerAccount') }}
            </button>
          </div>

          <div v-if="enableMobileRegister" class="val-login-type">
            <el-tooltip :content="$t('login.mobileLogin')" placement="top">
              <el-button
                :type="isMobileLogin ? 'primary' : 'default'"
                icon="el-icon-mobile-phone"
                circle
                @click="switchLoginType('mobile')"
              />
            </el-tooltip>
            <el-tooltip :content="$t('login.usernameLogin')" placement="top">
              <el-button
                :type="!isMobileLogin ? 'primary' : 'default'"
                icon="el-icon-user"
                circle
                @click="switchLoginType('username')"
              />
            </el-tooltip>
          </div>
        </div>
      </div>
    </section>

    <footer class="val-auth-footer">
      <version-footer />
    </footer>
  </div>
</template>

<script>
import Api from '@/apis/api';
import BrandLogo from '@/components/BrandLogo.vue';
import VersionFooter from '@/components/VersionFooter.vue';
import i18n, { changeLanguage } from '@/i18n';
import { goToPage, showDanger, showSuccess, sm2Encrypt, validateMobile } from '@/utils';
import { mapState } from 'vuex';

export default {
  name: 'login',
  components: { VersionFooter, BrandLogo },
  computed: {
    ...mapState({
      allowUserRegister: (state) => state.pubConfig.allowUserRegister,
      enableMobileRegister: (state) => state.pubConfig.enableMobileRegister,
      mobileAreaList: (state) => state.pubConfig.mobileAreaList,
      sm2PublicKey: (state) => state.pubConfig.sm2PublicKey,
      googleClientId: (state) => state.pubConfig.googleClientId,
    }),
    currentLanguage() {
      return i18n.locale || 'vi';
    },
    currentLanguageText() {
      return this.currentLanguage === 'en' ? this.$t('language.en') : this.$t('language.vi');
    },
    featureList() {
      return [
        this.$t('brand.feature.voice'),
        this.$t('brand.feature.agents'),
        this.$t('brand.feature.devices'),
      ];
    },
  },
  data() {
    return {
      form: {
        username: '',
        password: '',
        areaCode: '+86',
        mobile: '',
      },
      isMobileLogin: false,
      languageDropdownVisible: false,
    };
  },
  mounted() {
    localStorage.removeItem('pubConfig');
    this.$store.dispatch('fetchPubConfig').then(() => {
      this.isMobileLogin = this.enableMobileRegister;
      if (this.googleClientId) {
        this.loadGoogleSdk(() => {
          this.initGoogleSignIn();
        });
      }
    });
  },
  methods: {
    handleLanguageDropdownVisibleChange(visible) {
      this.languageDropdownVisible = visible;
    },
    changeLanguage(lang) {
      changeLanguage(lang);
      this.languageDropdownVisible = false;
      this.$message.success({ message: this.$t('message.success'), showClose: true });
      if (this.googleClientId) {
        this.loadGoogleSdk(() => {
          this.initGoogleSignIn();
        });
      }
    },
    switchLoginType(type) {
      this.isMobileLogin = type === 'mobile';
      this.form.username = '';
      this.form.mobile = '';
      this.form.password = '';
    },
    validateInput(input, messageKey) {
      if (!input.trim()) {
        showDanger(this.$t(messageKey));
        return false;
      }
      return true;
    },
    getUserInfo() {
      Api.user.getUserInfo(({ data }) => {
        if (data.code === 0) {
          this.$store.commit('setUserInfo', data.data);
          goToPage('/home');
        } else {
          showDanger(this.$t('login.userInfoFailed'));
        }
      });
    },
    async login() {
      if (this.isMobileLogin) {
        if (!validateMobile(this.form.mobile, this.form.areaCode)) {
          showDanger(this.$t('login.requiredMobile'));
          return;
        }
        this.form.username = this.form.areaCode + this.form.mobile;
      } else if (!this.validateInput(this.form.username, 'login.requiredUsername')) {
        return;
      }

      if (!this.validateInput(this.form.password, 'login.requiredPassword')) return;

      let encryptedPassword;
      try {
        encryptedPassword = sm2Encrypt(this.sm2PublicKey, this.form.password);
      } catch (error) {
        console.error("Password encryption failed:", error);
        showDanger(this.$t('sm2.encryptionFailed'));
        return;
      }

      Api.user.login(
        {
          username: this.form.username,
          password: encryptedPassword,
          captchaId: '',
        },
        ({ data }) => {
          showSuccess(this.$t('login.loginSuccess'));
          this.$store.commit('setToken', JSON.stringify(data.data));
          this.getUserInfo();
        },
        (err) => {
          showDanger(err.data?.msg || this.$t('login.loginFailed'));
        }
      );
    },
    goToRegister() {
      goToPage('/register');
    },
    goToForgetPassword() {
      goToPage('/retrieve-password');
    },
    loadGoogleSdk(callback) {
      // Remove any existing Google SDK script to force reload with new locale
      const existingScript = document.querySelector('script[src*="accounts.google.com/gsi/client"]');
      if (existingScript) {
        existingScript.remove();
        // Clear the cached google object so SDK reloads fresh
        delete window.google;
      }
      const script = document.createElement('script');
      const lang = this.$i18n.locale || 'vi';
      script.src = `https://accounts.google.com/gsi/client?hl=${lang}`;
      script.async = true;
      script.defer = true;
      script.onload = () => {
        callback();
      };
      script.onerror = () => {
        console.warn('Google Sign-In SDK failed to load');
      };
      document.head.appendChild(script);
    },
    triggerGoogleLogin() {
      // Custom button is replaced by official button, trigger is now handled by SDK.
      // Keeping this method as no-op to prevent any legacy caller issues.
    },
    initGoogleSignIn() {
      if (!window.google || !window.google.accounts) return;
      window.google.accounts.id.initialize({
        client_id: this.googleClientId,
        callback: this.handleGoogleCredentialResponse,
      });
      this.$nextTick(() => {
        const container = document.getElementById('google-btn-container');
        if (container) {
          container.innerHTML = '';
          const finalWidth = 320;
          const lang = this.$i18n.locale || 'vi';
          
          window.google.accounts.id.renderButton(
            container,
            {
              theme: "filled_black",
              size: "large",
              text: "continue_with",
              shape: "rectangular",
              logo_alignment: "left",
              width: finalWidth,
              locale: lang
            }
          );
        }
      });
    },
    handleGoogleCredentialResponse(response) {
      if (!response.credential) return;
      Api.user.googleLogin(
        response.credential,
        ({ data }) => {
          showSuccess(this.$t('login.loginSuccess'));
          this.$store.commit('setToken', JSON.stringify(data.data));
          this.getUserInfo();
        },
        (err) => {
          showDanger(err.data?.msg || this.$t('login.loginFailed'));
        }
      );
    },
  },
};
</script>

<style lang="scss" scoped>
@import './auth.scss';

.val-auth-form-wrapper {
  width: 100%;
  max-width: 320px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
}

.val-oauth-divider {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 20px 0 12px;
}

.val-divider-line {
  flex: 1;
  height: 1px;
  background: var(--val-border, rgba(255, 255, 255, 0.1));
}

.val-divider-text {
  font-size: 0.78rem;
  color: var(--val-text-dim, #64748b);
  white-space: nowrap;
  text-transform: lowercase;
}

.google-btn-container {
  width: 100%;
  display: flex;
  justify-content: center;
  min-height: 46px;
  
  ::v-deep iframe {
    margin: 0 auto;
  }
}
</style>
