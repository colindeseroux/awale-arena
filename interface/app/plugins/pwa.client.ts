import type { RuntimeNuxtHooks } from "#app";

export default defineNuxtPlugin(nuxtApp => {
    // Use proper PWA hook types with explicit type annotations
    nuxtApp.hook("pwa:ready" as keyof RuntimeNuxtHooks, () => {
        console.log("PWA is ready");
    });

    // Listen to PWA events using proper event handling
    if (import.meta.client) {
        // Check if service worker is supported
        if ("serviceWorker" in navigator) {
            navigator.serviceWorker.ready
                .then((registration: ServiceWorkerRegistration) => {
                    console.log("Service worker registered:", registration);
                })
                .catch((error: Error) => {
                    console.error("Service worker registration failed:", error);
                });

            navigator.serviceWorker.addEventListener("controllerchange", () => {
                console.log("Service worker activated");
            });
        }
    }
});
