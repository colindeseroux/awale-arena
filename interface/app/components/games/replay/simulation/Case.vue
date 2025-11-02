<script setup lang="ts">
const props = defineProps<{
    red: number;
    blue: number;
    transparent: number;
}>();
</script>

<template>
    <div
        style="
            flex: 1;
            max-width: 200px;
            aspect-ratio: 1 / 1;
            background: #582900;
            border: 1px solid #ccc;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
        "
    >
        <div
            style="
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                gap: 4px;
                width: 100%;
            "
        >
            <div
                style="
                    width: 32px;
                    height: 32px;
                    background: #fff;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    color: #222;
                    font-weight: bold;
                    font-size: 18px;
                    border: 1px solid #ccc;
                "
            >
                {{ props.transparent }}
            </div>
            <div
                style="
                    display: flex;
                    gap: 8px;
                    align-items: center;
                    justify-content: center;
                "
            >
                <div
                    style="
                        width: 32px;
                        height: 32px;
                        background: #e53935;
                        border-radius: 50%;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        color: #fff;
                        font-weight: bold;
                        font-size: 18px;
                    "
                >
                    {{ props.red }}
                </div>
                <div
                    style="
                        width: 32px;
                        height: 32px;
                        background: #1e88e5;
                        border-radius: 50%;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        color: #fff;
                        font-weight: bold;
                        font-size: 18px;
                    "
                >
                    {{ props.blue }}
                </div>
            </div>
        </div>
    </div>
</template>
