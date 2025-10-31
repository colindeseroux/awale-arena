export type Json =
    | string
    | number
    | boolean
    | null
    | { [key: string]: Json | undefined }
    | Json[];

export type Database = {
    // Allows to automatically instantiate createClient with right options
    // instead of createClient<Database, { PostgrestVersion: 'XX' }>(URL, KEY)
    __InternalSupabase: {
        PostgrestVersion: "13.0.5";
    };
    public: {
        Tables: {
            bots: {
                Row: {
                    activated: boolean;
                    commit_link: string | null;
                    created_at: string;
                    group: number;
                    id: number;
                    name: string;
                };
                Insert: {
                    activated?: boolean;
                    commit_link?: string | null;
                    created_at?: string;
                    group: number;
                    id?: number;
                    name: string;
                };
                Update: {
                    activated?: boolean;
                    commit_link?: string | null;
                    created_at?: string;
                    group?: number;
                    id?: number;
                    name?: string;
                };
                Relationships: [
                    {
                        foreignKeyName: "bots_group_fkey";
                        columns: ["group"];
                        isOneToOne: false;
                        referencedRelation: "groups";
                        referencedColumns: ["id"];
                    },
                ];
            };
            games: {
                Row: {
                    bot_1: number;
                    bot_2: number;
                    created_at: string;
                    id: number;
                    moves: string;
                    winner: number;
                };
                Insert: {
                    bot_1: number;
                    bot_2: number;
                    created_at?: string;
                    id?: number;
                    moves?: string;
                    winner?: number;
                };
                Update: {
                    bot_1?: number;
                    bot_2?: number;
                    created_at?: string;
                    id?: number;
                    moves?: string;
                    winner?: number;
                };
                Relationships: [
                    {
                        foreignKeyName: "games_bot_1_fkey";
                        columns: ["bot_1"];
                        isOneToOne: false;
                        referencedRelation: "bots";
                        referencedColumns: ["id"];
                    },
                    {
                        foreignKeyName: "games_bot_1_fkey";
                        columns: ["bot_1"];
                        isOneToOne: false;
                        referencedRelation: "daily_bot_scores";
                        referencedColumns: ["bot_id"];
                    },
                    {
                        foreignKeyName: "games_bot_2_fkey";
                        columns: ["bot_2"];
                        isOneToOne: false;
                        referencedRelation: "bots";
                        referencedColumns: ["id"];
                    },
                    {
                        foreignKeyName: "games_bot_2_fkey";
                        columns: ["bot_2"];
                        isOneToOne: false;
                        referencedRelation: "daily_bot_scores";
                        referencedColumns: ["bot_id"];
                    },
                ];
            };
            groups: {
                Row: {
                    id: number;
                    name: string;
                };
                Insert: {
                    id?: number;
                    name?: string;
                };
                Update: {
                    id?: number;
                    name?: string;
                };
                Relationships: [];
            };
            waitings: {
                Row: {
                    bot: number | null;
                    created_at: string;
                    id: number;
                };
                Insert: {
                    bot?: number | null;
                    created_at?: string;
                    id?: number;
                };
                Update: {
                    bot?: number | null;
                    created_at?: string;
                    id?: number;
                };
                Relationships: [
                    {
                        foreignKeyName: "queues_bot_fkey";
                        columns: ["bot"];
                        isOneToOne: false;
                        referencedRelation: "bots";
                        referencedColumns: ["id"];
                    },
                    {
                        foreignKeyName: "queues_bot_fkey";
                        columns: ["bot"];
                        isOneToOne: false;
                        referencedRelation: "daily_bot_scores";
                        referencedColumns: ["bot_id"];
                    },
                ];
            };
        };
        Views: {
            daily_bot_scores: {
                Row: {
                    bot_id: number | null;
                    group: number | null;
                    score: number | null;
                };
                Relationships: [
                    {
                        foreignKeyName: "bots_group_fkey";
                        columns: ["group"];
                        isOneToOne: false;
                        referencedRelation: "groups";
                        referencedColumns: ["id"];
                    },
                ];
            };
        };
        Functions: {
            [_ in never]: never;
        };
        Enums: {
            [_ in never]: never;
        };
        CompositeTypes: {
            [_ in never]: never;
        };
    };
};

type DatabaseWithoutInternals = Omit<Database, "__InternalSupabase">;

type DefaultSchema = DatabaseWithoutInternals[Extract<
    keyof Database,
    "public"
>];

export type Tables<
    DefaultSchemaTableNameOrOptions extends
        | keyof (DefaultSchema["Tables"] & DefaultSchema["Views"])
        | { schema: keyof DatabaseWithoutInternals },
    TableName extends DefaultSchemaTableNameOrOptions extends {
        schema: keyof DatabaseWithoutInternals;
    }
        ? keyof (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
              DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])
        : never = never,
> = DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals;
}
    ? (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
          DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])[TableName] extends {
          Row: infer R;
      }
        ? R
        : never
    : DefaultSchemaTableNameOrOptions extends keyof (DefaultSchema["Tables"] &
            DefaultSchema["Views"])
      ? (DefaultSchema["Tables"] &
            DefaultSchema["Views"])[DefaultSchemaTableNameOrOptions] extends {
            Row: infer R;
        }
          ? R
          : never
      : never;

export type TablesInsert<
    DefaultSchemaTableNameOrOptions extends
        | keyof DefaultSchema["Tables"]
        | { schema: keyof DatabaseWithoutInternals },
    TableName extends DefaultSchemaTableNameOrOptions extends {
        schema: keyof DatabaseWithoutInternals;
    }
        ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
        : never = never,
> = DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals;
}
    ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
          Insert: infer I;
      }
        ? I
        : never
    : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
      ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
            Insert: infer I;
        }
          ? I
          : never
      : never;

export type TablesUpdate<
    DefaultSchemaTableNameOrOptions extends
        | keyof DefaultSchema["Tables"]
        | { schema: keyof DatabaseWithoutInternals },
    TableName extends DefaultSchemaTableNameOrOptions extends {
        schema: keyof DatabaseWithoutInternals;
    }
        ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
        : never = never,
> = DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals;
}
    ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
          Update: infer U;
      }
        ? U
        : never
    : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
      ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
            Update: infer U;
        }
          ? U
          : never
      : never;

export type Enums<
    DefaultSchemaEnumNameOrOptions extends
        | keyof DefaultSchema["Enums"]
        | { schema: keyof DatabaseWithoutInternals },
    EnumName extends DefaultSchemaEnumNameOrOptions extends {
        schema: keyof DatabaseWithoutInternals;
    }
        ? keyof DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"]
        : never = never,
> = DefaultSchemaEnumNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals;
}
    ? DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"][EnumName]
    : DefaultSchemaEnumNameOrOptions extends keyof DefaultSchema["Enums"]
      ? DefaultSchema["Enums"][DefaultSchemaEnumNameOrOptions]
      : never;

export type CompositeTypes<
    PublicCompositeTypeNameOrOptions extends
        | keyof DefaultSchema["CompositeTypes"]
        | { schema: keyof DatabaseWithoutInternals },
    CompositeTypeName extends PublicCompositeTypeNameOrOptions extends {
        schema: keyof DatabaseWithoutInternals;
    }
        ? keyof DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"]
        : never = never,
> = PublicCompositeTypeNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals;
}
    ? DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"][CompositeTypeName]
    : PublicCompositeTypeNameOrOptions extends keyof DefaultSchema["CompositeTypes"]
      ? DefaultSchema["CompositeTypes"][PublicCompositeTypeNameOrOptions]
      : never;

export const Constants = {
    public: {
        Enums: {},
    },
} as const;
