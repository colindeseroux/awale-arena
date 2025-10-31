import type { ThemeDefinition } from "vuetify";
import colors from "vuetify/util/colors";

export const light: ThemeDefinition = {
    dark: false,
    colors: {
        primary: colors.blue.base,
        secondary: colors.grey.base,
        error: colors.red.base,
        info: colors.blue.base,
        success: colors.green.base,
        warning: colors.yellow.base,
        home: colors.shades.white,
    },
    variables: {},
};

export const dark: ThemeDefinition = {
    dark: true,
    colors: {
        primary: colors.blue.base,
        secondary: colors.grey.base,
        error: colors.red.base,
        info: colors.blue.base,
        success: colors.green.base,
        warning: colors.yellow.base,
        home: colors.shades.black,
    },
    variables: {},
};
