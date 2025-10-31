<script setup lang="ts">
import type { Game } from "@/types/game";
import BotInfos from "@/components/games/replay/BotInfos.vue";

const properties = defineProps<{
    game: Game;
}>();

const dialogOpen = ref(false);
</script>

<template>
    <VDialog v-model="dialogOpen" fullscreen>
        <template #activator="{ props }">
            <VBtn
                v-bind="props"
                color="success"
                dark
                prepend-icon="mdi-arrow-right-drop-circle"
                @click="dialogOpen = true"
            >
                {{ $t("replay.title") }}
            </VBtn>
        </template>

        <VCard class="d-flex flex-column">
            <VCardTitle>{{ $t("replay.title") }}</VCardTitle>

            <div class="d-flex flex-grow-1 gap-2 ma-2">
                <VCard class="bg-secondary mr-1" max-width="300px">
                    <VCardTitle>{{ $t("replay.info") }}</VCardTitle>

                    <VCardText>
                        <VCard>
                            <VCardText>
                                <p>
                                    <strong>{{ $t("replay.gameId") }}:</strong>
                                    {{ properties.game.id }}
                                </p>

                                <p>
                                    <strong>
                                        {{ $t("replay.createdAt") }}:
                                    </strong>
                                    {{
                                        properties.game.createdAt.toLocaleString()
                                    }}
                                </p>

                                <p>
                                    <strong>{{ $t("replay.winner") }}:</strong>
                                    {{
                                        properties.game.winner === 0
                                            ? $t("replay.draw")
                                            : properties.game.winner === 1
                                              ? properties.game.bot1.name
                                              : properties.game.bot2.name
                                    }}
                                </p>
                            </VCardText>
                        </VCard>

                        <VDivider />

                        <BotInfos :bot="properties.game.bot1" />

                        <VDivider />

                        <BotInfos :bot="properties.game.bot2" />
                    </VCardText>
                </VCard>

                <VCard class="bg-error" style="flex: 1">
                    <VCardTitle>{{ $t("replay.gameplay") }}</VCardTitle>

                    <VCardText>
                        <p>{{ properties.game.moves }}</p>
                    </VCardText>
                </VCard>
            </div>

            <VCardActions>
                <VSpacer />
                <VBtn
                    color="error"
                    variant="outlined"
                    @click="dialogOpen = false"
                >
                    {{ $t("replay.close") }}
                </VBtn>
            </VCardActions>
        </VCard>
    </VDialog>
</template>
