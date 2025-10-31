import type { Group } from "@/types/group";

export const useGroups = () => {
    const supabase = useSupabaseClient();
    const groups = ref<Group[]>([]);

    const getGroups = async () => {
        const { data: groups } = await supabase.from("groups").select();

        return groups as Group[];
    };

    const load = async () => {
        groups.value = await getGroups();
    };

    const subscription = supabase
        .channel("public:groups")
        .on(
            "postgres_changes",
            { event: "INSERT", schema: "public", table: "groups" },
            (payload: { new: Group }) => {
                groups.value.push(payload.new);
            },
        )
        .subscribe();

    onUnmounted(() => {
        subscription.unsubscribe();
    });

    load();

    return { groups };
};
