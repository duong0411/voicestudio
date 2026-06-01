<template>
  <div class="brand-logo" :class="[{ compact: compact }, sizeClass]">
    <div class="brand-mark" aria-hidden="true">
      <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="4" y="4" width="40" height="40" rx="12" fill="url(#valGrad)" />
        <path
          d="M14 28V20C14 17.8 15.8 16 18 16H22C24.2 16 26 17.8 26 20V28"
          stroke="white"
          stroke-width="2.2"
          stroke-linecap="round"
        />
        <path
          d="M26 24C28 22 30 21 33 21C36 21 38 23 38 26V28"
          stroke="#00E5C8"
          stroke-width="2.2"
          stroke-linecap="round"
        />
        <circle cx="18" cy="28" r="2" fill="#00E5C8" />
        <circle cx="33" cy="28" r="2" fill="white" />
        <defs>
          <linearGradient id="valGrad" x1="4" y1="4" x2="44" y2="44" gradientUnits="userSpaceOnUse">
            <stop stop-color="#7C5CFF" />
            <stop offset="1" stop-color="#00E5C8" />
          </linearGradient>
        </defs>
      </svg>
    </div>
    <div class="brand-text" v-if="!iconOnly">
      <span class="brand-name">Voice AL</span>
      <span class="brand-tag">Studio</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BrandLogo',
  props: {
    compact: { type: Boolean, default: false },
    iconOnly: { type: Boolean, default: false },
    size: { type: String, default: 'md' },
  },
  computed: {
    sizeClass() {
      return `size-${this.size}`;
    },
  },
};
</script>

<style lang="scss" scoped>
.brand-logo {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  user-select: none;

  &.compact {
    gap: 8px;
  }
}

.brand-mark svg {
  display: block;
  filter: drop-shadow(0 8px 20px rgba(124, 92, 255, 0.45));
}

.size-sm .brand-mark svg {
  width: 32px;
  height: 32px;
}

.size-md .brand-mark svg {
  width: 42px;
  height: 42px;
}

.size-lg .brand-mark svg {
  width: 56px;
  height: 56px;
}

.brand-text {
  display: flex;
  flex-direction: column;
  line-height: 1.05;
  text-align: left;
}

.brand-name {
  font-weight: 800;
  font-size: 1.15rem;
  letter-spacing: -0.02em;
  background: linear-gradient(135deg, #fff 0%, #c4b5fd 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.brand-tag {
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.28em;
  text-transform: uppercase;
  color: var(--val-accent, #00e5c8);
}

.size-lg .brand-name {
  font-size: 1.5rem;
}

.size-lg .brand-tag {
  font-size: 0.8rem;
}

.compact .brand-name {
  font-size: 1rem;
}
</style>
