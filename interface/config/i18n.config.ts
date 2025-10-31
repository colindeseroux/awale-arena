import { defineI18nConfig } from "nuxt-i18n";
import en from "@@/i18n/locales/en.json";
import fr from "@@/i18n/locales/fr.json";

export default defineI18nConfig({
    locales: [
        { code: "en", file: "en.json" },
        { code: "fr", file: "fr.json" },
    ],
    defaultLocale: "fr",
    strategy: "no_prefix",
    vueI18n: {
        fallbackLocale: "fr",
        messages: {
            en,
            fr,
        },
    },
});
