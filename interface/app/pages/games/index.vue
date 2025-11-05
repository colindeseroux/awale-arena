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
        title: t("games.createdAt"),
        key: "created_at",
        align: "center",
        value: item => item.createdAt.toLocaleString(),
    },
    {
        title: `${t("games.bot")} 1`,
        key: "bot_1",
        filterable: false,
        value: item => `${item.bot1.name} (${item.bot1.group.name})`,
    },
    {
        title: `${t("games.bot")} 2`,
        key: "bot_2",
        filterable: false,
        value: item => `${item.bot2.name} (${item.bot2.group.name})`,
    },
    {
        title: t("games.winner"),
        key: "winner",
        filterable: false,
        value: item =>
            item.winner === 0
                ? t("games.draw")
                : `${t("games.bot")} ${item.winner}`,
    },
]);
const search = ref<string>("");

const { formattedGames, isLoading } = compositedGames();

const filteredGames = computed(() => {
    if (!formattedGames.value) {
        return [];
    }

    if (!search.value) {
        return formattedGames.value;
    }

    const s = search.value.toLowerCase();

    return formattedGames.value.filter(game => {
        const dateStr = game.createdAt.toLocaleString().toLowerCase();
        const bot1Str =
            `${game.bot1.name} (${game.bot1.group.name})`.toLowerCase();
        const bot2Str =
            `${game.bot2.name} (${game.bot2.group.name})`.toLowerCase();
        const winnerStr =
            game.winner === 0
                ? t("games.draw").toLowerCase()
                : `${t("games.bot")} ${game.winner}`.toLowerCase();

        return (
            dateStr.includes(s) ||
            bot1Str.includes(s) ||
            bot2Str.includes(s) ||
            winnerStr.includes(s)
        );
    });
});
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
            class="games"
            fixed-header
            show-expand
            :headers="headers"
            :items="filteredGames"
            :mobile="(width as number) < 905"
        >
            <template #top>
                <VSheet
                    class="d-flex flex-wrap justify-space-between align-center pa-2"
                    color="secondary"
                >
                    <h1 class="ml-5">{{ $t("games.title") }}</h1>

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

                    <VDivider class="mx-4" inset vertical />

                    <GamesNewBot />
                </VSheet>
            </template>

            <template #[`item.bot_1`]="{ item, value }">
                <VChip
                    :border="`${item.winner === 0 ? 'primary' : item.winner === 1 ? 'success' : 'error'} thin opacity-25`"
                    :color="
                        item.winner === 0
                            ? 'primary'
                            : item.winner === 1
                              ? 'success'
                              : 'error'
                    "
                    :text="value"
                />
            </template>

            <template #[`item.bot_2`]="{ item, value }">
                <VChip
                    :border="`${item.winner === 0 ? 'primary' : item.winner === 2 ? 'success' : 'error'} thin opacity-25`"
                    :color="
                        item.winner === 0
                            ? 'primary'
                            : item.winner === 2
                              ? 'success'
                              : 'error'
                    "
                    :text="value"
                />
            </template>

            <template
                #[`item.data-table-expand`]="{
                    internalItem,
                    isExpanded,
                    toggleExpand,
                }"
            >
                <VBtn
                    icon
                    variant="text"
                    density="compact"
                    :aria-label="
                        isExpanded(internalItem)
                            ? $t('games.collapseRow')
                            : $t('games.expandRow')
                    "
                    @click="toggleExpand(internalItem)"
                >
                    <VIcon>
                        {{
                            isExpanded(internalItem)
                                ? "mdi-chevron-up"
                                : "mdi-chevron-down"
                        }}
                    </VIcon>
                </VBtn>
            </template>

            <template #expanded-row="{ item }">
                <tr>
                    <td />
                    <td>{{ item.bot1.commitLink }}</td>
                    <td>{{ item.bot2.commitLink }}</td>
                    <td>
                        <GamesReplay :game="item" />
                    </td>
                </tr>
            </template>
        </VDataTable>
    </VSkeletonLoader>
</template>

<style scoped>
.games {
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
