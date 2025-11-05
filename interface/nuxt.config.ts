import { light, dark } from "./app/plugins/vuetify/theme";

const sw = process.env.SW === "true";

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    app: {
        head: {
            title: "Awale Arena",
            meta: [
                {
                    name: "description",
                    content: "Awale Arena - A strategic board game",
                },
            ],
            htmlAttrs: {
                lang: "fr",
            },
        },
    },
    compatibilityDate: "2025-07-15",
    css: ["@/assets/styles/main.css"],
    devtools: { enabled: true },
    i18n: {
        locales: [
            { code: "en", language: "en-US", file: "en.json" },
            { code: "fr", language: "fr-FR", file: "fr.json" },
        ],
        defaultLocale: "fr",
        strategy: "no_prefix",
        vueI18n: "./config/i18n.config.ts",
    },
    modules: [
        "@nuxt/eslint",
        "@nuxtjs/i18n",
        "vuetify-nuxt-module",
        "@vite-pwa/nuxt",
        "@nuxtjs/supabase",
        "vite-plugin-checker",
    ],
    pwa: {
        strategies: sw ? "injectManifest" : "generateSW",
        srcDir: sw ? "service-worker" : undefined,
        filename: sw ? "sw.ts" : undefined,
        registerType: "autoUpdate",
        manifest: {
            name: "Awale Arena",
            short_name: "Awale Arena",
            description: "Awale Arena - A strategic board game",
            theme_color: "#1976d2",
            background_color: "#ffffff",
            display: "standalone",
            orientation: "portrait",
            scope: "/",
            start_url: "/",
            lang: "fr",
            id: "/",
            icons: [
                {
                    src: "/img/icons/pwa-192x192.png",
                    sizes: "192x192",
                    type: "image/png",
                },
                {
                    src: "/img/icons/pwa-256x256.png",
                    sizes: "256x256",
                    type: "image/png",
                },
                {
                    src: "/img/icons/pwa-384x384.png",
                    sizes: "384x384",
                    type: "image/png",
                },
                {
                    src: "/img/icons/pwa-512x512.png",
                    sizes: "512x512",
                    type: "image/png",
                    purpose: "any maskable",
                },
            ],
        },
        workbox: {
            navigateFallback: "/",
            globPatterns: ["**/*.{js,css,html,png,svg,ico}"],
            cleanupOutdatedCaches: true,
            skipWaiting: true,
            clientsClaim: true,
        },
        injectManifest: {
            globPatterns: ["**/*.{js,css,html,png,svg,ico}"],
        },
        client: {
            installPrompt: true,
            periodicSyncForUpdates: 20,
        },
        devOptions: {
            enabled: true,
            suppressWarnings: true,
            navigateFallback: "/",
            navigateFallbackAllowlist: [/^\/$/],
            type: "module",
        },
        includeAssets: ["img/icons/*.png"],
    },
    runtimeConfig: {
        public: {
            allowedFileExtensions:
                process.env.ALLOWED_FILE_EXTENSIONS?.split(",") || [],
            apiBaseUrl: process.env.API_BASE_URL,
            supabaseKey: process.env.SUPABASE_KEY,
            supabaseUrl: process.env.SUPABASE_URL,
        },
    },
    srcDir: "app",
    supabase: {
        redirect: false,
    },
    typescript: {
        typeCheck: true,
    },
    vuetify: {
        moduleOptions: {
            ssrClientHints: {
                prefersColorScheme: true,
                prefersColorSchemeOptions: {
                    darkThemeName: "dark",
                    lightThemeName: "light",
                },
            },
        },
        vuetifyOptions: {
            theme: {
                defaultTheme: "light",
                themes: {
                    light: light,
                    dark: dark,
                },
            },
        },
    },
});
