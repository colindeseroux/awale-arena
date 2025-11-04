<script lang="ts" setup>
import type { DataTableHeader } from "vuetify";
import { VSkeletonLoader } from "vuetify/components";

definePageMeta({
    layout: "dashboard",
});

const { t } = useI18n();
const { width } = useDisplay();

const headers = computed<DataTableHeader[]>(() => [
    {
        title: t("scoreboard.bot"),
        key: "bot",
        value: item => `${item.bot.name} (${item.bot.group.name})`,
    },
    {
        title: t("scoreboard.day"),
        key: "day",
        value: item => item.day.toLocaleDateString(),
    },
    {
        title: t("scoreboard.score"),
        key: "score",
        value: item => item.score.toFixed(2),
    },
]);
const search = ref<string>("");

const { formattedScores, isLoading } = compositedScores();

const filteredScores = computed(() => {
    if (!formattedScores.value) {
        return [];
    }

    if (!search.value) {
        return formattedScores.value;
    }

    const s = search.value.toLowerCase();

    return formattedScores.value.filter(score => {
        const dateStr = score.day.toLocaleString().toLowerCase();
        const botStr =
            `${score.bot.name} (${score.bot.group.name})`.toLowerCase();

        return dateStr.includes(s) || botStr.includes(s);
    });
});

const getScoreColor = (score: number) => {
    if (score >= 0.99) {
        return "#42a5f5";
    }

    if (score >= 0.65) {
        const t = (score - 0.65) / (1 - 0.65);
        return interpolateColor("#43a047", "#42a5f5", t);
    }

    if (score >= 0.35) {
        const t = (score - 0.35) / (0.65 - 0.35);
        return interpolateColor("#ffa726", "#43a047", t);
    }

    const t = score / 0.35;

    return interpolateColor("#e53935", "#ffa726", t);
};

const interpolateColor = (color1: string, color2: string, t: number) => {
    const c1 = hexToRgb(color1);
    const c2 = hexToRgb(color2);
    const r = Math.round(c1.r + (c2.r - c1.r) * t);
    const g = Math.round(c1.g + (c2.g - c1.g) * t);
    const b = Math.round(c1.b + (c2.b - c1.b) * t);

    return `rgb(${r},${g},${b})`;
};

const hexToRgb = (hex: string) => {
    hex = hex.replace("#", "");

    if (hex.length === 3) {
        hex = hex
            .split("")
            .map(x => x + x)
            .join("");
    }

    const num = parseInt(hex, 16);

    return {
        r: (num >> 16) & 255,
        g: (num >> 8) & 255,
        b: num & 255,
    };
};
</script>

<template>
    <VSkeletonLoader
        class="mx-2 mt-2"
        boilerplate
        elevation="2"
        type="table-heading, table-thead, table-tbody, table-tfoot"
        :loading="isLoading"
    >
        <VDataTable
            class="scores"
            fixed-header
            :headers="headers"
            :items="filteredScores"
            :mobile="(width as number) < 905"
        >
            <template #top>
                <VSheet
                    class="d-flex flex-wrap justify-space-between align-center pa-2"
                    color="secondary"
                >
                    <h1 class="ml-5">{{ $t("scoreboard.title") }}</h1>

                    <VDivider class="mx-4" inset vertical />

                    <VTextField
                        v-model="search"
                        append-icon="mdi-magnify"
                        class="search"
                        min-width="270"
                        max-width="500"
                        single-line
                        hide-details
                        :label="$t('games.search')"
                    />

                    <div />
                </VSheet>
            </template>

            <template #[`item.score`]="{ item, value }">
                <VChip
                    :border="`${getScoreColor(item.score)} thin opacity-25`"
                    :color="getScoreColor(item.score)"
                    :text="value"
                />
            </template>
        </VDataTable>
    </VSkeletonLoader>
</template>

<style scoped>
.scores {
    height: calc(100vh - 27px);
    width: calc(100vw - 97px);
    border-radius: 8px;
    box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.45);
    margin: 10px;

    .v-sheet {
        border-radius: 8px 8px 0 0;
    }
}
</style>
