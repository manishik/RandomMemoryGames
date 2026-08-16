import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    // Firefox may resolve localhost to IPv4 while Vite defaults to IPv6 only.
    // Binding to the IPv6 wildcard accepts both IPv6 and IPv4 connections.
    host: '::',
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
