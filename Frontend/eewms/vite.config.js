import path from "path";
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { VitePWA } from "vite-plugin-pwa";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    VitePWA({
      injectRegister: "auto",
      registerType: "autoUpdate",
      workbox: { clientsClaim: true, skipWaiting: true }
    })
  ],
  server: {
    port: 3000
  },
  build: {
    chunkSizeWarningLimit: 2000
  },
  resolve: {
    alias: {
      app_develop: "/src/app_develop"
    }
  }
});
