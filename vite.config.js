import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 3000
  },
  optimizeDeps: {
    // 排除 child_process 模块
    exclude: ['child_process']
  },
  build: {
    commonjsOptions: {
      include: []
    },
    // 关闭摇树优化，避免错误移除相关模块
    rollupOptions: {
      treeshake: false
    }
  }
})
