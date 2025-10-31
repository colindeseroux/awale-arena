import type { Group } from "@/types/group";

export interface SupabaseBot {
    id: number;
    created_at: string;
    commit_link: string;
    name: string;
    group: number;
    activated: boolean;
}

export interface Bot {
    id: number;
    createdAt: Date;
    commitLink: string;
    name: string;
    group: Group;
    activated: boolean;
}

export interface AddBotPayload {
    name: string;
    commitLink: string;
    group: { name: string };
}
