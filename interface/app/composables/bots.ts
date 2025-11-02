import type { SupabaseBot } from "@/types/bot";

export const useBots = () => {
    const supabase = useSupabaseClient();
    const bots = ref<SupabaseBot[]>([]);
    const botsFetched = ref(false);

    const getBots = async () => {
        const { data: bots } = await supabase.from("bots").select();

        return bots as SupabaseBot[];
    };

    const load = async () => {
        bots.value = await getBots();
        botsFetched.value = true;
    };

    const subscription = supabase
        .channel("public:bots")
        .on(
            "postgres_changes",
            { event: "INSERT", schema: "public", table: "bots" },
            (payload: { new: SupabaseBot }) => {
                bots.value.push(payload.new);
            },
        )
        .subscribe();

    onUnmounted(() => {
        subscription.unsubscribe();
    });

    load();

    return { bots, botsFetched };
};
