import { getCookie, setCookie } from "h3";

export default defineEventHandler(event => {
    let theme = getCookie(event, "color-scheme");

    if (!theme) {
        theme = "light";
        setCookie(event, "color-scheme", theme, { path: "/", httpOnly: false });
    }

    event.context.theme = theme;
});
