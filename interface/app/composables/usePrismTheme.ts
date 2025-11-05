import { onMounted, watchEffect } from "vue";
import { useTheme } from "vuetify";

export function usePrismTheme() {
    const theme = useTheme();

    const prismDark = new URL(
        "@/assets/prism-themes/prism-one-dark.css",
        import.meta.url,
    ).href;
    const prismLight = new URL(
        "@/assets/prism-themes/prism-one-light.css",
        import.meta.url,
    ).href;

    onMounted(() => {
        watchEffect(() => {
            document
                .querySelectorAll("link[data-prism-theme]")
                .forEach(el => el.remove());

            const themeCSS = theme.global.current.value.dark
                ? prismDark
                : prismLight;

            const link = document.createElement("link");
            link.rel = "stylesheet";
            link.href = themeCSS;
            link.setAttribute("data-prism-theme", "true");
            document.head.appendChild(link);
        });
    });
}
