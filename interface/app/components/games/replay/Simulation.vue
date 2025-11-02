<script setup lang="ts">
import type { Game } from "@/types/game";
import Case from "@/components/games/replay/simulation/Case.vue";
import SimulationGame from "@/utils/simulationGame";

const props = defineProps<{
    game: Game;
}>();

const simulation = ref(new SimulationGame());
const currentMoveIndex = ref(0);
const automaticallyPlay = ref(false);

const rows = computed(() => [
    simulation.value.red.slice(0, 8).map((_, i) => i),
    simulation.value.red.slice(8, 16).map((_, i) => 15 - i),
]);

const previousMove = () => {
    simulation.value.previousMove();
    currentMoveIndex.value--;
};

const nextMove = () => {
    const move = props.game.moves[currentMoveIndex.value]!;

    simulation.value.applyMove(move);
    currentMoveIndex.value++;
};

const playMove = () => {
    if (
        automaticallyPlay.value &&
        currentMoveIndex.value < props.game.moves.length
    ) {
        nextMove();
        setTimeout(playMove, 500);
    }
};
</script>

<template>
    <VCard color="home" style="flex: 1">
        <VCardTitle>{{ $t("games.replay.simulation.title") }}</VCardTitle>

        <VCardItem>
            <p>
                {{ $t("games.replay.simulation.movePlayed") }}:
                {{ props.game.moves[currentMoveIndex - 1] }}
            </p>
            <p>
                {{ $t("games.replay.simulation.nextMove") }}:
                {{ props.game.moves[currentMoveIndex] }}
            </p>
            <p>
                {{ props.game.bot1.name }} ({{ props.game.bot1.group.name }})
                <strong class="mx-2">
                    {{ simulation.score[0] }} - {{ simulation.score[1] }}
                </strong>
                {{ props.game.bot2.name }} ({{ props.game.bot2.group.name }})
            </p>
        </VCardItem>

        <VCardItem>
            <div class="simulation-grid">
                <div
                    v-for="(row, rowIndex) in rows"
                    :key="rowIndex"
                    class="simulation-row"
                >
                    <Case
                        v-for="idx in row"
                        :key="idx"
                        :red="simulation.red[idx] ?? 0"
                        :blue="simulation.blue[idx] ?? 0"
                        :transparent="simulation.transparent[idx] ?? 0"
                    />
                </div>
            </div>
        </VCardItem>

        <VCardActions class="d-flex justify-space-evenly my-2">
            <VBtn
                class="mt-2"
                color="primary"
                prepend-icon="mdi-arrow-left-bold-hexagon-outline"
                variant="outlined"
                :disabled="currentMoveIndex < 1"
                @click="previousMove()"
            >
                <template v-slot:prepend>
                    <v-icon size="x-large"></v-icon>
                </template>
                {{ $t("games.replay.simulation.previousMove") }}
            </VBtn>
            <VBtn
                color="primary"
                size="large"
                variant="outlined"
                :icon="
                    automaticallyPlay ? 'mdi-pause-circle' : 'mdi-play-circle'
                "
                :rounded="!automaticallyPlay ? 'circle' : 'lg'"
                @click="((automaticallyPlay = !automaticallyPlay), playMove())"
            ></VBtn>
            <VBtn
                class="mt-2"
                append-icon="mdi-arrow-right-bold-hexagon-outline"
                color="primary"
                variant="outlined"
                :disabled="currentMoveIndex >= props.game.moves.length"
                @click="nextMove()"
            >
                {{ $t("games.replay.simulation.nextMove") }}
                <template v-slot:append>
                    <v-icon size="x-large"></v-icon>
                </template>
            </VBtn>
        </VCardActions>

        <VCardText>
            <p>{{ props.game.moves }}</p>
        </VCardText>
    </VCard>
</template>

<style scoped>
.simulation-grid {
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: center;
    width: 100%;
}

.simulation-row {
    display: flex;
    gap: 8px;
    width: 100%;
}
</style>
