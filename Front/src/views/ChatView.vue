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
    <!--DIALOG-->
    <template>
      <v-row justify="center">
        <v-dialog
            v-model="notConnectedDialog"
            persistent
            max-width="30vw"
        >
          <v-card>
            <v-card-title class="text-h5" v-if="!roomCfg.full">
              <v-icon class="mr-3" large>mdi-chat-question-outline</v-icon>
              Do you want to join this chat room?
            </v-card-title>
            <v-card-title class="text-h5" v-else>
              <v-icon class="mr-3" large>mdi-emoticon-sad-outline</v-icon>
              We are sorry but the room is full, try again later
            </v-card-title>
            <v-row v-if="!roomCfg.full" class="ma-4">
              <v-col>
                <v-text-field
                    prepend-inner-icon="mdi-account"
                    v-model="joinDto.nickname"
                    :rules="rules.name"
                    label="Your nickname"
                    required
                    filled
                ></v-text-field>
              </v-col>
              <v-col v-if="roomCfg.hasPassword">
                <v-text-field
                    prepend-inner-icon="mdi-lock"
                    v-model="joinDto.password"
                    :rules="rules.password"
                    label="Room password"
                    required
                    filled
                ></v-text-field>
              </v-col>
            </v-row>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                  color="green darken-1"
                  text
                  @click="$router.push(`/`);"
              >
                Exit
              </v-btn>
              <v-btn
                  color="green darken-1"
                  text
                  @click="notConnectedDialog = false"
                  v-if="!roomCfg.full"
              >
                Join
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-row>
    </template>
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
    chatMessages: [],
    chatUsers: [],
    notConnectedDialog: false,
    roomCfg: {
      hasPassword: false,
      full: false
    },
    joinDto: {
      nickname: '',
      password: ''
    },
    rules: {
      name: [
        v => !!v || 'Nickname is required',
        v => !(v && v.length > 14) || 'Nickname must be less than 14 characters',
        v => !(v && v.length < 4) || 'Nickname must be at least 4 characters',
      ],
      password: [
          v => !!v || 'Password is requited'
      ]
    },
  }),
  created() {
    this.existsRoom();

    if (this.isSessionSetted() && !this.roomMatch()) {
      this.validConnection = false;
      EventBus.$emit('showAlert', {
        color: "error",
        type: "error",
        msg: `You cannot access this chat room.`
      });
      this.$router.push(`/404`);
    }

    if (!this.isSessionSetted()){
      this.validConnection = false;
      this.notConnectedDialog = true;
    }
    //this.myId = this.getUserId(); <-- En principio no se usa, borrar si veo que no afecta a nada
    dayjs.extend(relativeTime);

    if (this.validConnection) this.connectsWS();
  },
  methods: {
    connectsWS(){
      this.socket = new SockJS('http://localhost:9876/websocket', null, {
        sessionId: () => {
          return this.getChatId() + ":" + this.getUserId() + ":" + this.getNickname() + ":" + Math.floor(Math.random() * 99999);
        }
      });
      this.stompClient = Stomp.over(this.socket);
      //this.stompClient.debug = () => { };
      this.stompClient.connect({
            senderId: this.getUserId(),
            token: this.token,
            senderNickname: this.getNickname(),
            lobby: this.getChatId(),
          },
          frame => {
            this.stompClient.subscribe(`/topic/messages/${this.getChatId()}`, tick => {
              console.log(JSON.parse(tick.body))
              //this.filterLobbyInteraction(JSON.parse(tick.body));
            });
            //EventBus.$emit('reloadPlayersInLobby');
          },
          error => {
            EventBus.$emit('showAlert', {
              color: "error",
              type: "error",
              msg: "Error connecting to the chat socket"
            });
          });
    },
    roomMatch() {
      return (this.$route.params.roomId.toUpperCase() === this.getChatId().toUpperCase());
    },
    existsRoom(){
      this.axios
        .get(`/v1/cfg/check/${this.$route.params.roomId}`)
        .then((res) => {
          this.roomCfg = res.data;
          if (this.roomCfg.full){
            EventBus.$emit('showAlert', {
              color: "info",
              type: "info",
              msg: `The room is full, try again later`
            });
          }
        })
        .catch((e) => {
          EventBus.$emit('showAlert', {
            color: "error",
            type: "error",
            msg: `Error ${e.response.data.code}. ${e.response.data.message}`
          });
          this.$router.push(`/404`);
        });
    },

    handleRoomCfg(){

    }
  }
}
</script>