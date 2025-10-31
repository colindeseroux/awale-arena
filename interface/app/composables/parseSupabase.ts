import type { Bot } from "@/types/bot";
import type { Game } from "@/types/game";

export const compositedGames = () => {
    const { games } = useGames();
    const { bots } = useBots();
    const { groups } = useGroups();

    const formattedGames = computed(() => {
        const gamesList: Game[] = [];

        for (const game of games.value) {
            const bot1 = bots.value.find(b => b.id === game.bot_1);
            const bot2 = bots.value.find(b => b.id === game.bot_2);

            if (!bot1 || !bot2) {
                return null;
            }

            const group1 = groups.value.find(g => g.id === bot1?.group);
            const group2 = groups.value.find(g => g.id === bot2?.group);

            if (!group1 || !group2) {
                return null;
            }

            const compositedBot1: Bot = {
                id: bot1.id,
                createdAt: new Date(bot1.created_at),
                commitLink: bot1.commit_link,
                name: bot1.name,
                group: group1,
                activated: bot1.activated,
            };

            const compositedBot2: Bot = {
                id: bot2.id,
                createdAt: new Date(bot2.created_at),
                commitLink: bot2.commit_link,
                name: bot2.name,
                group: group2,
                activated: bot2.activated,
            };

            const compositedGame: Game = {
                id: game.id,
                bot1: compositedBot1,
                bot2: compositedBot2,
                moves: parseMoves(game.moves),
                winner: game.winner,
                createdAt: new Date(game.created_at),
            };

            gamesList.push(compositedGame);
        }

        return gamesList;
    });

    const isLoading = computed(
        () => !bots.value.length || !groups.value.length || !games.value.length,
    );

    return { formattedGames, isLoading };
};
