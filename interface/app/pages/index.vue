<script setup lang="ts">
import { useI18n } from "vue-i18n";

definePageMeta({
    layout: "dashboard",
});

const { tm } = useI18n();

const rules = tm("home.rules") as {
    title: string;
    description: string[];
}[];
</script>

<template>
    <div class="pa-6">
        <h1 class="text-h4 font-weight-bold mb-8 text-center">
            {{ $t("home.title") }}
        </h1>

        <div>
            <section
                v-for="(rule, i) in rules"
                :key="i"
                class="pa-6 rounded-lg mb-6 transition elevation-1 bg-homeSecondary"
            >
                <h2 class="text-h6 font-weight-bold d-flex align-center mb-3">
                    <VIcon color="primary" class="mr-2">
                        mdi-book-outline
                    </VIcon>
                    {{ $t(`home.rules.${i}.title`) }}
                </h2>

                <ul class="pl-8">
                    <li
                        v-for="(_, j) in rule.description"
                        :key="j"
                        class="text-body-2 d-flex align-start"
                        :class="
                            $t(`home.rules.${i}.description.${j}`).includes(
                                '- ',
                            )
                                ? ''
                                : 'mt-2'
                        "
                    >
                        <span>
                            {{ $t(`home.rules.${i}.description.${j}`) }}
                        </span>
                    </li>
                </ul>
            </section>
        </div>
    </div>
</template>
