<template>
  <v-card
      tile
      elevation="0"
      class="messagesContainer pa-0 ma-0"
      height="84vh"
  >
    <v-list>
      <v-list-item three-line
                   v-for="msg in messages"
                   :key="msg.uid"
      >
        <v-list-item-content>
          <v-card
              max-width="35vw"
              :color="(myId === msg.senderId)?'blue':'grey'"
              dark
              :class="(myId === msg.senderId)?'ml-auto':'mr-auto'"
          >
            <v-list-item three-line>
              <v-list-item-content>
                <div class="mb-4">
                  <v-icon small class="mb-1 mr-2">mdi-account</v-icon>
                  <strong>{{ msg.senderNick }}</strong>
                </div>
                <p>
                  {{ msg.msg }}
                </p>
                <v-list-item-subtitle class="mt-4">
                  <v-icon small class="mb-1 mr-2">mdi-clock-outline</v-icon>
                  {{ timeFromDate(msg.time).charAt(0).toUpperCase() + timeFromDate(msg.time).slice(1) }}
                </v-list-item-subtitle>
              </v-list-item-content>
              <v-badge
                  bordered
                  bottom
                  color="green"
                  dot
                  offset-x="10"
                  offset-y="10"
                  class="chat-msg-badge"
              >
                <v-avatar
                    color="grey"
                    class="lighten-2"
                    size="50">
                  <v-img height="50" :src="`https://avatars.dicebear.com/api/${msg.avatarUrl}/${msg.senderNick}.svg`"></v-img>
                </v-avatar>
              </v-badge>
            </v-list-item>
          </v-card >
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-card>
</template>
<style scoped>
.messagesContainer{
  overflow-y: auto !important;
}
</style>
<script>
import { mixins } from "@/mixins";
import { EventBus } from "@/main";
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
export default {
  mixins: [mixins],
  name: "ChatMessages",
  data: () => ({
    myId: '',
    messages: [
      {
        senderId: 'a759609f',
        senderNick: 'Xavi',
        time: new Date(),
        msg: 'Hola que tal, soy colosal',
        avatarUrl: 'big-ears',
        uid: 'as96s0d'
      },
      {
        senderId: 'f422sfl8',
        senderNick: 'Paula',
        time: new Date(),
        msg: '',
        avatarUrl: 'bottts',
        uid: 'k3hfh31'
      },
      {
        senderId: 'a759609f',
        senderNick: 'Xavi',
        time: new Date(),
        msg: 'Puedo prometer y prometo...',
        avatarUrl: 'big-ears',
        uid: 'dlk1d34v'
      },
      {
        senderId: 'a759609f',
        senderNick: 'Xavi',
        time: new Date(),
        msg: 'Hola que tal, soy colosal',
        avatarUrl: 'big-ears',
        uid: 'as96s0d'
      },
      {
        senderId: 'f422sfl8',
        senderNick: 'Paula',
        time: new Date(),
        msg: '',
        avatarUrl: 'bottts',
        uid: 'k3hfh31'
      },
      {
        senderId: 'a759609f',
        senderNick: 'Xavi',
        time: new Date(),
        msg: 'Puedo prometer y prometo...',
        avatarUrl: 'big-ears',
        uid: 'dlk1d34v'
      },
      {
        senderId: 'a759609f',
        senderNick: 'Xavi',
        time: new Date(),
        msg: 'Hola que tal, soy colosal',
        avatarUrl: 'big-ears',
        uid: 'as96s0d'
      },
      {
        senderId: 'f422sfl8',
        senderNick: 'Paula',
        time: new Date(),
        msg: '',
        avatarUrl: 'bottts',
        uid: 'k3hfh31'
      },
      {
        senderId: 'a759609f',
        senderNick: 'Xavi',
        time: new Date(),
        msg: 'Puedo prometer y prometo...',
        avatarUrl: 'big-ears',
        uid: 'dlk1d34v'
      },
    ]
  }),
  created() {
    this.myId = this.getUserId();
  },
  methods: {
    timeFromDate(date){
      return dayjs(date).fromNow();
    },
  }
}
</script>