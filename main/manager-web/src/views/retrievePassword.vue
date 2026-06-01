<template>
  <div class="welcome" @keyup.enter="retrievePassword">
    <el-container style="height: 100%;">
      <el-header>
        <div style="display: flex;align-items: center;margin-top: 15px;margin-left: 10px;gap: 10px;">
          <BrandLogo size="md" />
        </div>
      </el-header>
      <div class="login-person">
        <img loading="lazy" alt="" src="@/assets/login/login-person.png" style="width: 100%;" />
      </div>
      <el-main style="position: relative;">
        <form @submit.prevent="retrievePassword">
          <div class="login-box">
            <div style="display: flex;align-items: center;gap: 20px;margin-bottom: 39px;padding: 0 30px;">
              <img loading="lazy" alt="" src="@/assets/login/hi.png" style="width: 34px;height: 34px;" />
              <div class="login-text">{{ $t('retrievePassword.title') }}</div>
              <div class="login-welcome">
                {{ $t('retrievePassword.subtitle') }}
              </div>
            </div>

            <div style="padding: 0 30px;">
              <div class="input-box">
                <img loading="lazy" alt="" class="input-icon" src="@/assets/login/username.png" />
                <el-input v-model="form.username" :placeholder="$t('login.usernamePlaceholder')" />
              </div>

              <div class="input-box">
                <img loading="lazy" alt="" class="input-icon" src="@/assets/login/password.png" />
                <el-input
                  v-model="form.newPassword"
                  :placeholder="$t('retrievePassword.newPasswordPlaceholder')"
                  type="password"
                  show-password
                />
              </div>

              <div class="input-box">
                <img loading="lazy" alt="" class="input-icon" src="@/assets/login/password.png" />
                <el-input
                  v-model="form.confirmPassword"
                  :placeholder="$t('retrievePassword.confirmNewPasswordPlaceholder')"
                  type="password"
                  show-password
                />
              </div>

              <div style="font-weight: 400;font-size: 14px;text-align: left;color: #5778ff;margin-top: 20px;">
                <div style="cursor: pointer;" @click="goToLogin">{{ $t('retrievePassword.goToLogin') }}</div>
              </div>
            </div>

            <div class="login-btn" @click="retrievePassword">{{ $t('retrievePassword.resetButton') }}</div>
          </div>
        </form>
      </el-main>

      <el-footer>
        <version-footer />
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import Api from '@/apis/api';
import BrandLogo from '@/components/BrandLogo.vue';
import VersionFooter from '@/components/VersionFooter.vue';
import { goToPage, showDanger, showSuccess, sm2Encrypt } from '@/utils';
import { mapState } from 'vuex';

export default {
  name: 'retrieve',
  components: {
    VersionFooter,
    BrandLogo,
  },
  computed: {
    ...mapState({
      sm2PublicKey: state => state.pubConfig.sm2PublicKey,
    }),
  },
  data() {
    return {
      form: {
        username: '',
        newPassword: '',
        confirmPassword: '',
      },
    };
  },
  mounted() {
    this.$store.dispatch('fetchPubConfig');
  },
  methods: {
    retrievePassword() {
      if (!this.form.username.trim()) {
        showDanger(this.$t('login.requiredUsername'));
        return;
      }
      if (!this.form.newPassword.trim()) {
        showDanger(this.$t('retrievePassword.newPasswordRequired'));
        return;
      }
      if (this.form.newPassword !== this.form.confirmPassword) {
        showDanger(this.$t('retrievePassword.passwordsNotMatch'));
        return;
      }

      let encryptedPassword;
      try {
        encryptedPassword = sm2Encrypt(this.sm2PublicKey, this.form.newPassword);
      } catch (error) {
        console.error('Error encrypting password:', error);
        showDanger(this.$t('sm2.encryptionFailed'));
        return;
      }

      Api.user.retrievePassword(
        {
          username: this.form.username.trim(),
          password: encryptedPassword,
          captchaId: '',
        },
        () => {
          showSuccess(this.$t('retrievePassword.passwordUpdateSuccess'));
          goToPage('/login');
        },
        (err) => {
          showDanger(err.data?.msg || this.$t('message.error'));
        }
      );
    },

    goToLogin() {
      goToPage('/login');
    },
  },
};
</script>

<style lang="scss" scoped>
@import './auth.scss';
</style>
