import type { Group } from "@/types/group";
import type { Bot } from "@/types/bot";

export interface SupabaseScore {
    bot_id: number;
    group: number;
    day: string;
    score: number;
}

export interface Score {
    bot: Bot;
    day: Date;
    score: number;
}
