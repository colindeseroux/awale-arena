import type { Bot } from "@/types/bot";

export interface SupabaseGame {
    id: number;
    bot_1: number;
    bot_2: number;
    moves: string;
    winner: number;
    created_at: string;
}

export interface Game {
    id: number;
    bot1: Bot;
    bot2: Bot;
    moves: string[];
    winner: number;
    createdAt: Date;
}
