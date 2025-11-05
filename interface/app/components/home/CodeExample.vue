<script setup lang="ts">
const codeSnippets = ref<Record<string, { lang: string; code: string }>>({});
const activeTab = ref<string>("java");
const copying = ref(false);

const loadFiles = async () => {
    try {
        const resFiles = await $fetch<{ files: string[] }>("/api/files");
        const files = [];

        for (const filename of resFiles.files) {
            const resContent = await $fetch<{ content: string }>(
                `/api/files/${filename}`,
            );
            files.push({
                name: filename,
                content: resContent.content,
            });
        }

        codeSnippets.value = Object.fromEntries(
            files.map(f => [
                f.name.split(".")[0],
                { lang: f.name.split(".")[1], code: f.content },
            ]),
        );
        activeTab.value = files[0]!.name.split(".")[0]!;
    } catch (err) {
        console.error(err);
    }
};

const copyCode = async () => {
    const code = codeSnippets.value[activeTab.value]?.code || "";
    await navigator.clipboard.writeText(code);
    copying.value = true;
    setTimeout(() => (copying.value = false), 1000);
};

onMounted(() => {
    loadFiles();
});
</script>

<template>
    <VCard class="max-w-md mx-auto mt-5" elevation="3">
        <VCardTitle>
            <div class="d-flex align-center ma-1 mt-3">
                <div class="bg-red button-circle" />
                <div class="bg-yellow button-circle mx-2" />
                <div class="bg-green button-circle" />
            </div>

            <VDivider class="mt-4" />
        </VCardTitle>

        <VCardItem class="pt-0">
            <VTabs v-model="activeTab" grow>
                <VTab
                    v-for="(code, lang) in codeSnippets"
                    :key="lang"
                    :value="lang"
                >
                    {{ lang.toUpperCase() }}
                </VTab>
            </VTabs>
        </VCardItem>

        <VCardItem class="pt-0">
            <VBtn
                icon="mdi-content-copy"
                class="copy-btn"
                :class="{ copying: copying }"
                variant="text"
                @click="copyCode"
            />
            <Prism :key="activeTab" :language="codeSnippets[activeTab]?.lang">
                {{ codeSnippets[activeTab]?.code }}
            </Prism>
        </VCardItem>
    </VCard>
</template>

<style scoped>
.button-circle {
    width: 12px;
    height: 12px;
    border-radius: 100%;
}

.copy-btn {
    position: absolute;
    top: 130px;
    right: 20px;
    transition:
        transform 0.3s,
        background-color 0.3s;
}

.copying {
    background-color: #4caf50 !important;
    transform: scale(1.2);
}
</style>
