<template>
  <v-carousel
      :cycle=true
      :hide-delimiters=true
      interval="12000"
      :show-arrows=false
      height="38vh"
  >
    <v-carousel-item
        v-for="(item,i) in items"
        :key="i"
    >
      <v-parallax
          :src="item.src"
      >
      </v-parallax>
    </v-carousel-item>
  </v-carousel>
</template>

<script>
export default {
  name: "CaruselHome.vue",
  data: () => ({
    model: 0,
    items: [
      {
        title: 'Lorem Ipsum',
        description: 'Lorem14',
        src: 'https://unsplash.com/photos/3iT3dnmblGE/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjcxOTAxNjYy&force=true&w=1920'
      },
      {
        src: 'https://unsplash.com/photos/b18TRXc8UPQ/download?ixid=MnwxMjA3fDB8MXxzZWFyY2h8NjR8fGNvZGV8ZXN8MHx8fHwxNjcxOTAyNDMz&force=true&w=1920'
      }
    ],
  }),
}
</script>

<style scoped>

</style>