<template>
  <v-card width="100%"
          elevation="0"
          class="text-msg-input"
  >
    <v-text-field
        v-model="description"
        append-outer-icon="mdi-send"
        @click:append-outer="sendMessage()"
        outlined
    ></v-text-field>
  </v-card>
</template>
<script>
export default {
  name: "ChatInput",
  methods: {
    sendMessage(){
      console.log("clickooo")
    }
  }
}
</script>

<style scoped>
</style>