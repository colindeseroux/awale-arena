import type { Bot } from "@/types/bot";
import type { Game } from "@/types/game";
import type { Score } from "@/types/score";

export const compositedGames = () => {
    const { games, gamesFetched } = useGames();
    const { bots, botsFetched } = useBots();
    const { groups, groupsFetched } = useGroups();

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
        () => !gamesFetched.value || !botsFetched.value || !groupsFetched.value,
    );

    return { formattedGames, isLoading };
};

export const compositedScores = () => {
    const { scores, scoresFetched } = useScores();
    const { groups, groupsFetched } = useGroups();
    const { bots, botsFetched } = useBots();

    const formattedScores = computed(() => {
        const scoresList: Score[] = [];

        for (const score of scores.value) {
            const bot = bots.value.find(b => b.id === score.bot_id);

            if (!bot) {
                return null;
            }

            const group = groups.value.find(g => g.id === score.group);

            if (!group) {
                return null;
            }

            const compositedBot: Bot = {
                id: bot.id,
                createdAt: new Date(bot.created_at),
                commitLink: bot.commit_link,
                name: bot.name,
                group: group,
                activated: bot.activated,
            };

            const compositedScore: Score = {
                bot: compositedBot,
                day: new Date(score.day),
                score: score.score,
            };

            scoresList.push(compositedScore);
        }

        return scoresList;
    });

    const isLoading = computed(
        () =>
            !scoresFetched.value || !groupsFetched.value || !botsFetched.value,
    );

    return { formattedScores, isLoading };
};
