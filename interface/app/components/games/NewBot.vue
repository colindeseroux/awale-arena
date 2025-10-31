<script setup lang="ts">
import type { AddBotPayload } from "@/types/bot";
import CustomVTextField from "@/components/CustomVTextField.vue";

const { t } = useI18n();
const theme = useTheme();
const config = useRuntimeConfig();

const cookieGroupName = useCookie("groupName");

const dialogOpen = ref(false);
const error = ref<string | null>(null);
const rules = {
    required: (value: string) => !!value || t("newBot.fieldIsRequired"),
};

const bot = ref<AddBotPayload>({
    name: "",
    commitLink: "",
    group: { name: cookieGroupName.value || "" },
});

const file = ref<File | null>(null);
const fileInput = ref<HTMLInputElement | null>(null);
const borderColor = computed(() => {
    return file.value
        ? theme.current.value.colors.primary
        : theme.current.value.colors.error;
});

const handleDrop = (event: DragEvent) => {
    event.preventDefault();
    const droppedFile = event.dataTransfer?.files[0];

    if (droppedFile) {
        file.value = droppedFile;
    }
};

const handleFileChange = (event: Event) => {
    const target = event.target as HTMLInputElement;
    const selectedFile = target.files?.[0];

    if (selectedFile) {
        file.value = selectedFile;
    }
};

const addBot = async () => {
    if (!file.value) {
        error.value = "newBot.pleaseUploadFile";
        return;
    }

    if (!bot.value.name || !bot.value.group.name) {
        error.value = "newBot.fieldsAreRequired";
        return;
    }

    const formData = new FormData();
    formData.append(
        "bot",
        new Blob([JSON.stringify(bot.value)], { type: "application/json" }),
    );
    formData.append("file", file.value);

    await $fetch("/bot", {
        baseURL: config.public.apiBaseUrl,
        method: "POST",
        body: formData,
    })
        .then(() => {
            cookieGroupName.value = bot.value.group.name;
            error.value = null;
            bot.value = {
                name: "",
                commitLink: "",
                group: { name: bot.value.group.name },
            };
            file.value = null;
            dialogOpen.value = false;
        })
        .catch(err => {
            error.value = `newBot.${err.response._data}`;
        });
};
</script>

<template>
    <VDialog v-model="dialogOpen" max-width="600px">
        <template #activator="{ props }">
            <VBtn
                v-bind="props"
                color="success"
                dark
                prepend-icon="mdi-plus"
                @click="dialogOpen = true"
            >
                {{ $t("newBot.newBot") }}
            </VBtn>
        </template>

        <VCard>
            <VCardTitle>{{ $t("newBot.newBot") }}</VCardTitle>

            <VCardItem>
                <CustomVTextField
                    v-model="bot.name"
                    icon="$vuetify"
                    :label="$t('newBot.name')"
                    :required="true"
                    :rules="[rules.required]"
                />
                <CustomVTextField
                    v-model="bot.commitLink"
                    icon="$vuetify"
                    :label="$t('newBot.commitLink')"
                />
                <CustomVTextField
                    v-model="bot.group.name"
                    icon="mdi-account"
                    :label="$t('newBot.group')"
                    :required="true"
                    :rules="[rules.required]"
                />

                <div
                    class="file-drop-area"
                    :style="{
                        border: '2px dashed',
                        borderColor: borderColor,
                        padding: '1rem',
                        textAlign: 'center',
                        marginTop: '1rem',
                        cursor: 'pointer',
                    }"
                    @drop="handleDrop"
                    @dragover.prevent
                    @click="fileInput?.click()"
                >
                    <p v-if="!file">
                        {{ $t("newBot.dragAndDrop") }}
                    </p>
                    <p v-else>
                        {{ $t("newBot.selectedFile", { fileName: file.name }) }}
                    </p>
                    <input
                        ref="fileInput"
                        type="file"
                        style="display: none"
                        @change="handleFileChange"
                    >
                </div>
            </VCardItem>

            <VCardItem>
                <VAlert v-if="error" type="error" variant="outlined" dense>
                    {{ $t(error) }}
                </VAlert>
            </VCardItem>

            <VCardActions>
                <VSpacer />
                <VBtn
                    color="error"
                    variant="outlined"
                    @click="dialogOpen = false"
                >
                    {{ $t("newBot.cancel") }}
                </VBtn>
                <VBtn color="success" variant="outlined" @click="addBot">
                    {{ $t("newBot.adds") }}
                </VBtn>
            </VCardActions>
        </VCard>
    </VDialog>
</template>
