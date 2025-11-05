<script setup lang="ts">
const cookieLanguage = useCookie("language");
const { current } = useLocale();

const switchLanguage = computed({
    get() {
        return cookieLanguage.value ? cookieLanguage.value : "fr";
    },
    set(value: string) {
        current.value = value;
        cookieLanguage.value = value;
    },
});

const componentRef = ref<HTMLElement | null>(null);
const componentWidth = ref(0);
let resizeObserver: ResizeObserver | null = null;

onMounted(() => {
    if (componentRef.value) {
        resizeObserver = new ResizeObserver(entries => {
            for (const entry of entries) {
                const { width } = entry.contentRect;
                componentWidth.value = width;
            }
        });
        resizeObserver.observe(componentRef.value);
    }
});

onBeforeUnmount(() => {
    if (resizeObserver && componentRef.value) {
        resizeObserver.unobserve(componentRef.value);
    }
});
</script>

<template>
    <div ref="componentRef">
        <VBtnToggle
            v-if="componentWidth > 40"
            v-model="switchLanguage"
            aria-label="Switch language"
            class="changeLanguage"
            density="comfortable"
            mandatory
        >
            <VBtn
                aria-label="Français"
                :color="$i18n.locale === 'fr' ? 'primary' : ''"
                style="min-width: 36px; padding: 0 8px"
                title="Change language to French"
                value="fr"
                variant="text"
            >
                <span style="font-size: 20px">🇫🇷</span>
            </VBtn>
            <VBtn
                aria-label="English"
                :color="$i18n.locale === 'en' ? 'primary' : ''"
                style="min-width: 36px; padding: 0 8px"
                title="Change language to English"
                value="en"
                variant="text"
            >
                <span style="font-size: 20px">🇬🇧</span>
            </VBtn>
        </VBtnToggle>

        <div v-else>
            <p>{{ $i18n.locale.toUpperCase() }}</p>
        </div>
    </div>
</template>

<style scoped>
.changeLanguage {
    border-radius: 16px;
    overflow: hidden;
    border: 1px solid #e0e0e0;
    background: transparent;
    box-shadow: none;
}
</style>
