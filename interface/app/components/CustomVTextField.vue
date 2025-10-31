<script setup lang="ts">
import type { ValidationRule } from "vuetify";
import { defineProps, defineEmits } from "vue";

const props = defineProps({
    modelValue: { type: [String, Number], default: "" },
    icon: { type: String, default: "" },
    label: { type: String, default: "" },
    required: { type: Boolean, default: false },
    rules: { type: Array as () => ValidationRule[], default: () => [] },
});

const emit = defineEmits(["update:modelValue"]);
</script>

<template>
    <VTextField
        :model-value="props.modelValue"
        class="mt-2"
        variant="outlined"
        :base-color="
            props.modelValue !== '' || !props.required ? 'primary' : 'error'
        "
        :color="
            props.modelValue !== '' || !props.required ? 'primary' : 'error'
        "
        :label="props.label"
        :prepend-inner-icon="props.icon"
        :rules="props.rules"
        @update:model-value="val => emit('update:modelValue', val)"
    />
</template>
