<script setup lang="ts">
import type { Bot } from "@/types/bot";

const props = defineProps<{
    bot: Bot;
}>();
</script>

<template>
    <VCard class="mt-2">
        <VCardText>
            <p>
                <strong>{{ $t("botInfos.name") }}:</strong> {{ props.bot.name }}
            </p>
            <p>
                <strong>{{ $t("botInfos.group") }}:</strong>
                {{ props.bot.group.name }}
            </p>
            <p>
                <strong>{{ $t("botInfos.createdAt") }}:</strong>
                {{ props.bot.createdAt.toLocaleString() }}
            </p>
            <p>
                <strong>{{ $t("botInfos.commitLink") }}:</strong>
                {{ props.bot.commitLink }}
            </p>
        </VCardText>
    </VCard>
</template>
