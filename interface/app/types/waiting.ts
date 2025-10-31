import type { Bot } from "@/types/bot";

export interface SupabaseWaiting {
    id: number;
    created_at: string;
    bot: number;
}

export interface Waiting {
    id: number;
    createdAt: Date;
    bot: Bot;
}
