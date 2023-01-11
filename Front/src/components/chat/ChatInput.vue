<template>
  <v-card width="100%"
          elevation="0"
          class="text-msg-input"
  >
    <v-text-field
        v-model="chatInput"
        append-outer-icon="mdi-send"
        @click:append-outer="sendMessage(chatInput)"
        outlined
    ></v-text-field>
  </v-card>
</template>
<script>
import {EventBus} from "@/main";

export default {
  name: "ChatInput",
  data: () => ({
    chatInput: '',
  }),
  methods: {
    sendMessage(msg){
      EventBus.$emit('chatInput_chatView_sendMessage',
      msg
      );
    }
  }
}
</script>

<style scoped>
</style>