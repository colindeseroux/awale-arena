<script setup lang="ts">
const theme = useTheme();
const cookieTheme = useCookie("color-scheme");

const switchTheme = computed({
    get() {
        return theme.global.name.value;
    },
    set(value: string) {
        theme.change(value);
        cookieTheme.value = value;
    },
});
</script>

<template>
    <VSwitch
        v-model="switchTheme"
        class="switchTheme"
        :aria-label="`Switch to ${
            $vuetify.theme.global.name === 'dark' ? 'light' : 'dark'
        } theme`"
        false-value="dark"
        inset
        :prepend-icon="
            $vuetify.theme.global.name === 'dark'
                ? 'mdi-weather-night'
                : 'mdi-white-balance-sunny'
        "
        true-value="light"
        :title="`Switch to ${
            $vuetify.theme.global.name === 'dark' ? 'light' : 'dark'
        } theme`"
    />
</template>

<style scoped>
.switchTheme {
    margin-bottom: -30px;
}
</style>
