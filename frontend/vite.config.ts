import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    port: 5173,
    // dev: same-origin calls to /api go to the Spring backend (no CORS needed)
    proxy: { '/api': { target: 'http://localhost:8080', changeOrigin: true } },
  },
})
