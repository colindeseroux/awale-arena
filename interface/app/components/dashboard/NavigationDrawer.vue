<script setup lang="ts">
import SwitchTheme from "@/components/dashboard/SwitchTheme.vue";
import SwitchLanguage from "@/components/dashboard/SwitchLanguage.vue";

const cookieGroup = useCookie("groupName");
</script>

<template>
    <VNavigationDrawer
        aria-label="Main navigation"
        expand-on-hover
        mobile-breakpoint="xs"
        rail
    >
        <VList aria-label="Groups" density="compact" nav role="menu">
            <VListItem role="menuitem" prepend-icon="mdi-account">
                <div>{{ cookieGroup ? cookieGroup : "-----" }}</div>
            </VListItem>
        </VList>

        <VDivider />

        <VList
            aria-label="Home"
            density="compact"
            nav
            role="menu"
            :disabled="$route.path === '/'"
        >
            <VListItem role="menuitem" prepend-icon="mdi-home" to="/">
                <div>{{ $t("navigationDrawer.home") }}</div>
            </VListItem>
        </VList>

        <VDivider />

        <VList
            aria-label="Games"
            density="compact"
            nav
            role="menu"
            :disabled="$route.path === '/games'"
        >
            <VListItem
                role="menuitem"
                prepend-icon="mdi-nintendo-game-boy"
                to="/games"
            >
                <div>{{ $t("navigationDrawer.games") }}</div>
            </VListItem>
        </VList>

        <VDivider />

        <VList aria-label="Settings" density="compact" nav role="menu">
            <VListItem role="menuitem">
                <ClientOnly>
                    <SwitchTheme />
                </ClientOnly>
            </VListItem>
            <VListItem role="menuitem">
                <ClientOnly>
                    <SwitchLanguage />
                </ClientOnly>
            </VListItem>
        </VList>
    </VNavigationDrawer>
</template>
