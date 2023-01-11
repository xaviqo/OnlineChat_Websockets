<template>
  <v-card
      tile
      dark
      elevation="0"
  >
    <v-img
        height="55vh"
        src="https://unsplash.com/photos/nF8xhLMmg0c/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjcxODk0NDk2&force=true&w=640"
    >
      <v-row fluid class="fill-height">
        <v-col class="d-flex justify-center align-center">
          <v-btn class="button-title-color" light x-large outlined elevation="8">
            <v-card-title primary-title>
              Join a lobby and start chatting
            </v-card-title>
          </v-btn>
        </v-col>
      </v-row>
    </v-img>
  </v-card>
</template>

<script>
export default {
  name: 'PublicLobbiesCard',
  data: () => ({
    loading: false,
    selection: 1,
  }),

  methods: {
    reserve () {
      this.loading = true
      setTimeout(() => (this.loading = false), 2000)
    },
  },
}
</script>

<style scoped>
.button-title-color {
  background-color: rgba(255, 255, 255, .7) !important;
  border-color: white !important;
}
</style>