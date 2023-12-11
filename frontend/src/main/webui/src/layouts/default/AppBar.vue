<template>
  <v-app-bar flat>
    <router-link class="app-title-router-link" to="/">
      <v-app-bar-title>
        <v-icon icon="mdi-currency-usd" />
        Purchase Application
      </v-app-bar-title>
   </router-link>
    <template v-slot:append>
        <v-btn to="/purchase"><v-icon icon="mdi-currency-usd"></v-icon>Purchase</v-btn>
        <v-btn to="/customers"><v-icon icon="mdi-account"></v-icon>Customers</v-btn>
        <v-btn to="/products"><v-icon icon="mdi-gift-outline"></v-icon>Products</v-btn>
    </template>
  </v-app-bar>
</template>

<script lang="ts" setup>
  //
</script>

<style lang="css">

.app-title-router-link { 
  text-decoration: none;
}

</style>
