import { defineNuxtPlugin } from "#app";
import VuePrismComponent from "vue-prism-component";
import Prism from "prismjs";

import "prismjs/components/prism-java";
import "prismjs/components/prism-python";
import "prismjs/components/prism-c";
import "prismjs/components/prism-cpp";

import "prism-themes/themes/prism-one-light.css";

export default defineNuxtPlugin(nuxtApp => {
    nuxtApp.vueApp.component("Prism", VuePrismComponent);
    nuxtApp.provide("prism", Prism);
});
