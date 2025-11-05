<script setup lang="ts">
import type { Game } from "@/types/game";

const properties = defineProps<{
    game: Game;
}>();
</script>

<template>
    <VCard class="mr-2" color="home" max-width="300px">
        <VCardTitle>{{ $t("games.replay.infos.title") }}</VCardTitle>

        <VCardText>
            <VCard>
                <VCardText>
                    <p>
                        <strong>{{ $t("games.replay.infos.gameId") }}:</strong>
                        {{ properties.game.id }}
                    </p>

                    <p>
                        <strong>
                            {{ $t("games.replay.infos.createdAt") }}:
                        </strong>
                        {{ properties.game.createdAt.toLocaleString() }}
                    </p>

                    <p>
                        <strong>{{ $t("games.replay.infos.winner") }}:</strong>
                        {{
                            properties.game.winner === 0
                                ? $t("games.replay.infos.draw")
                                : properties.game.winner === 1
                                  ? properties.game.bot1.name
                                  : properties.game.bot2.name
                        }}
                    </p>
                </VCardText>
            </VCard>

            <VDivider />

            <GamesReplayInfosBotInfos :bot="properties.game.bot1" />

            <VDivider />

            <GamesReplayInfosBotInfos :bot="properties.game.bot2" />
        </VCardText>
    </VCard>
</template>
