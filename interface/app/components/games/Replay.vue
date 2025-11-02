<script setup lang="ts">
import type { Game } from "@/types/game";
import Infos from "@/components/games/replay/Infos.vue";
import Simulation from "@/components/games/replay/Simulation.vue";

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
                {{ $t("games.replay.title") }}
            </VBtn>
        </template>

        <VCard class="d-flex">
            <VCardTitle>{{ $t("games.replay.title") }}</VCardTitle>

            <div class="d-flex flex-row flex-grow-1 gap-2 ma-2">
                <Infos :game="properties.game" />

                <Simulation :game="properties.game" />
            </div>

            <VCardActions>
                <VSpacer />
                <VBtn
                    color="error"
                    variant="outlined"
                    @click="dialogOpen = false"
                >
                    {{ $t("games.replay.close") }}
                </VBtn>
            </VCardActions>
        </VCard>
    </VDialog>
</template>
