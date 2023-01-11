<template>
  <div class="overflow-hidden">
    <v-row>
      <v-col cols="2">
        <ChatUsers></ChatUsers>
      </v-col>
      <v-divider vertical></v-divider>
      <v-col cols="8">
        <v-container>
          <ChatMessages></ChatMessages>
        </v-container>
        <v-container>
          <ChatInput></ChatInput>
        </v-container>
      </v-col>
      <v-divider vertical></v-divider>
      <v-col cols="2">
        <ChatConfiguration></ChatConfiguration>
      </v-col>
    </v-row>
  </div>
</template>
<style>
* {
  overflow: hidden !important;
}
</style>
<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import { mixins } from "@/mixins";
import { EventBus } from "@/main";
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import ChatUsers from "@/components/chat/ChatUsers.vue";
import ChatMessages from "@/components/chat/ChatMessages.vue";
import ChatConfiguration from "@/components/chat/ChatConfiguration.vue";
import ChatInput from "@/components/chat/ChatInput.vue";
export default {
  mixins: [mixins],
  components: {
    ChatInput,
    ChatConfiguration,
    ChatUsers,
    ChatMessages

  },
  data: () => ({
    validConnection: true,
    socket: null,
    stompClient: null,
    chatUsers: []
  }),
  created() {
    this.existsRoom();

    if (this.isSessionSetted()){
      //si está inscrito checkear si está en la sala y tomar accion
      this.isUserInscribed();
    } else {
      this.$router.push("/join/room/"+this.$route.params.roomId);
    }

    EventBus.$on('chatInput_chatView_sendMessage', msg => {
      this.sendMessage(msg);
    });

    dayjs.extend(relativeTime);
    if (this.validConnection) this.connectsWS();
  },
  methods: {
    connectsWS(){
      this.socket = new SockJS('http://localhost:8080/websocket', null, {
        sessionId: () => {
          return this.$route.params.roomId + ":" + this.getUserId() + ":" + this.getNickname() + ":" + Math.floor(Math.random() * 99999);
        }
      });
      this.stompClient = Stomp.over(this.socket);
      //this.stompClient.debug = () => { };
      this.stompClient.connect({
            senderId: this.getUserId(),
            token: this.getAccessToken(),
            senderName: this.getNickname(),
            roomId: this.$route.params.roomId,
          },
          frame => {
            this.stompClient.subscribe(`/topic/messages/${this.$route.params.roomId}`, tick => {
              EventBus.$emit('chatView_chatMessages_filterMessage', JSON.parse(tick.body));
            });
          },
          error => {
            EventBus.$emit('showAlert', {
              color: "error",
              type: "error",
              msg: "Error connecting to the chat socket"
            });
          });
    },
    sendMessage(message){
      //TODO: https://stomp-js.github.io/stomp-websocket/codo/class/Client.html#send-dynamic
      // MIRAR LO DEL BODY
      this.stompClient.send(`/ws/chat/${this.$route.params.roomId}`,
          JSON.stringify({
            senderId: this.getUserId(),
            senderName: this.getNickname(),
            message: message
          }),"");
    },
    existsRoom(){
      this.axios
          .get(`/v1/check/room-status/${this.$route.params.roomId}`)
          .catch((e) => {
            EventBus.$emit('showAlert', {
              color: "error",
              type: "error",
              msg: `Error ${e.response.data.code}. ${e.response.data.message}`
            });
            this.$router.push(`/404`);
          });
    },
    isUserInscribed(){
      this.axios
          .get(`/v1/check/user-inscribed/${this.$route.params.roomId}`)
          .then((res) => {
            this.validConnection = true;
          })
          .catch((e) => {
            this.validConnection = false;
            this.$router.push("/join/room/"+this.$route.params.roomId);
          });
    },
  }
}
</script>