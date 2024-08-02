<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script scoped>
export default {
  name: 'App',
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

a {
  text-decoration: none;
}

#app {
  font-family: 'Consolas';
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}

html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>
