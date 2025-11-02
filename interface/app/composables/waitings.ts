import type { RealtimePostgresChangesFilter } from "@supabase/supabase-js";
import type { SupabaseWaiting } from "@/types/waiting";

export const useWaitings = () => {
    const supabase = useSupabaseClient();
    const waitings = ref<SupabaseWaiting[]>([]);
    const waitingsFetched = ref(false);

    const getWaitings = async () => {
        const { data: waitings } = await supabase.from("waitings").select();

        return waitings as SupabaseWaiting[];
    };

    const insertFilter: RealtimePostgresChangesFilter<"INSERT"> = {
        event: "INSERT",
        schema: "public",
        table: "waitings",
    };

    const deleteFilter: RealtimePostgresChangesFilter<"DELETE"> = {
        event: "DELETE",
        schema: "public",
        table: "waitings",
    };

    const load = async () => {
        waitings.value = await getWaitings();
        waitingsFetched.value = true;
    };

    const subscriptionInsert = supabase
        .channel("public:waitings")
        .on("postgres_changes", insertFilter, payload => {
            waitings.value.push(payload.new as SupabaseWaiting);
        })
        .subscribe();

    const subscriptionDelete = supabase
        .channel("public:waitings")
        .on("postgres_changes", deleteFilter, payload => {
            waitings.value = waitings.value.filter(
                w => w.id !== (payload.old as SupabaseWaiting).id,
            );
        })
        .subscribe();

    onUnmounted(() => {
        subscriptionInsert.unsubscribe();
        subscriptionDelete.unsubscribe();
    });

    load();

    return { waitings };
};
