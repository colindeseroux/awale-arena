import type { SupabaseGame } from "@/types/game";

export const useGames = () => {
    const supabase = useSupabaseClient();
    const games = ref<SupabaseGame[]>([]);

    const getGames = async () => {
        const { data: gamesData } = await supabase.from("games").select();

        return gamesData as SupabaseGame[];
    };

    const load = async () => {
        games.value = await getGames();
    };

    const subscription = supabase
        .channel("public:games")
        .on(
            "postgres_changes",
            { event: "INSERT", schema: "public", table: "games" },
            (payload: { new: SupabaseGame }) => {
                games.value.push(payload.new);
            },
        )
        .subscribe();

    onUnmounted(() => {
        subscription.unsubscribe();
    });

    load();

    return { games };
};
