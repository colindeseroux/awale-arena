import { onUnmounted } from "vue";
import { useTheme } from "vuetify";
import type { H3Event } from "h3";

export function useThemeDetection(event?: H3Event) {
    const theme = useTheme();

    // Server side: set theme from middleware
    if (import.meta.server && event?.context?.theme) {
        theme.change(event.context.theme);
    }

    // Client side: if no cookie, detect browser theme
    if (import.meta.client) {
        const mediaQuery = window.matchMedia("(prefers-color-scheme: dark)");
        const userPrefersDark = mediaQuery.matches;
        const cookieTheme = useCookie("color-scheme");

        if (!cookieTheme.value) {
            cookieTheme.value = userPrefersDark ? "dark" : "light";
        }

        theme.change(cookieTheme.value);

        // Watch for changes in color scheme preference
        const handleColorSchemeChange = (e: MediaQueryListEvent) => {
            const newTheme = e.matches ? "dark" : "light";
            cookieTheme.value = newTheme;
            theme.change(newTheme);
        };

        mediaQuery.addEventListener("change", handleColorSchemeChange);

        // Cleanup listener on component unmount
        onUnmounted(() => {
            mediaQuery.removeEventListener("change", handleColorSchemeChange);
        });
    }
}
