<template>
  <v-app class="v-app">
    <app-bar></app-bar>
    <div class="router-view-wrapper">    
      <router-view/> 
    </div>

  </v-app>
</template>

<script lang="ts" setup>
import AppBar from './layouts/default/AppBar.vue';
  //
</script>

<style lang="css">
  .router-view-wrapper { 
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
  }
</style>
