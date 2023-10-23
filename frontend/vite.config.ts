import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react({})],
  server: {
    proxy: {
      '/api': {
        target: 'http://ec2-15-165-55-217.ap-northeast-2.compute.amazonaws.com',
        changeOrigin: true,
        secure: false,
        rewrite: path => path.replace(/^\/api/, ''),
      },
    },
  },
})
