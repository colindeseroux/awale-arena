import type { SupabaseScore } from "@/types/score";

export const useScores = () => {
    const supabase = useSupabaseClient();
    const scores = ref<SupabaseScore[]>([]);
    const scoresFetched = ref(false);

    const getScores = async () => {
        const { data: scoresData } = await supabase.from("scores").select();

        return scoresData as SupabaseScore[];
    };

    const load = async () => {
        scores.value = await getScores();
        scoresFetched.value = true;
    };

    const subscription = supabase
        .channel("public:scores")
        .on(
            "postgres_changes",
            { event: "INSERT", schema: "public", table: "scores" },
            (payload: { new: SupabaseScore }) => {
                const exist = scores.value.find(
                    s =>
                        s.bot_id === payload.new.bot_id &&
                        s.day === payload.new.day,
                );

                if (exist) {
                    Object.assign(exist, payload.new);
                } else {
                    scores.value.push(payload.new);
                }
            },
        )
        .subscribe();

    onUnmounted(() => {
        subscription.unsubscribe();
    });

    load();

    return { scores, scoresFetched };
};
