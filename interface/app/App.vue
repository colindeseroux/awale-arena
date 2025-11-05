<script setup lang="ts">
const event = useRequestEvent();

useThemeDetection(event);
usePrismTheme();
</script>

<template>
    <NuxtPwaManifest />
    <NuxtLoadingIndicator />
    <NuxtRouteAnnouncer />
    <VApp>
        <VMain aria-label="Main content" role="main">
            <NuxtLayout>
                <NuxtPage />
            </NuxtLayout>
        </VMain>
    </VApp>
</template>
