import type { ThemeDefinition } from "vuetify";
import colors from "vuetify/util/colors";

export const light: ThemeDefinition = {
    dark: false,
    colors: {
        primary: colors.blue.base,
        secondary: colors.blue.darken4,
        error: colors.red.base,
        info: colors.blue.base,
        success: colors.green.base,
        warning: colors.yellow.base,
        home: colors.shades.white,
        homeSecondary: colors.grey.lighten3,
    },
    variables: {},
};

export const dark: ThemeDefinition = {
    dark: true,
    colors: {
        primary: colors.blue.base,
        secondary: colors.blue.darken4,
        error: colors.red.base,
        info: colors.blue.base,
        success: colors.green.base,
        warning: colors.yellow.base,
        home: colors.shades.black,
        homeSecondary: colors.grey.darken4,
    },
    variables: {},
};
