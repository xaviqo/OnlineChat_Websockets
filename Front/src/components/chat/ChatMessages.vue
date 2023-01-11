<template>
  <v-card
      tile
      elevation="0"
      class="messagesContainer pa-0 ma-0"
      height="84vh"
  >
    <v-list ref="msgListRef">
      <v-list-item three-line
                   v-for="msg in messages"
                   :key="msg.uid"
      >
        <v-list-item-content>
          <v-card
              max-width="35vw"
              :color="messageColor(msg)"
              dark
              :class="whereGoesMessage(msg)"
          >
            <v-list-item three-line>
              <v-list-item-content>
                <div class="mb-4">
                  <v-icon small class="mb-1 mr-2">mdi-account</v-icon>
                  <strong>{{ msg.senderName }}</strong>
                  <span v-if="msg.action == 'SPAM'"> | <b>Spam:</b> <i>{{msg.spamType}}</i></span>
                </div>
                <p>
                  {{ msg.message }}
                </p>
                <v-list-item-subtitle class="mt-4">
                  <v-icon small class="mb-1 mr-2">mdi-clock-outline</v-icon>
                  {{ timeFromDate(msg.time).charAt(0).toUpperCase() + timeFromDate(msg.time).slice(1) }}
                </v-list-item-subtitle>
              </v-list-item-content>
              <v-badge
                  v-if="msg.action == 'MESSAGE'"
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
    messages: [],
    adminData: {
      adminId: "",
      adminNickname: "",
      itsMe: ""
    },
    actionsToSave: []
  }),
  created() {
    this.myId = this.getUserId();
    this.getAdminIdAndName();

    EventBus.$on('chatView_chatMessages_filterMessage', body => {
      console.log(body);
      if (this.saveMessage(body.action)) this.messages.push(body);
      this.filterMessage(body);
      this.scrollChat();
    });

    setInterval( () => this.$forceUpdate(), 28500);
  },
  methods: {
    filterMessage(body){
      switch (body.action){
        case  "JOIN":
          if (!this.isMyId(body.senderId)){
            EventBus.$emit('showAlert', {
              color: "green",
              type: "info",
              msg: body.popMessage
            });
          }
          break;
        case "LEAVE":
          EventBus.$emit('showAlert', {
            color: "green",
            type: "info",
            msg: body.popMessage
          });
          break;
        case "SPAM":
          if (this.isMyId(body.senderId) || this.adminData.itsMe){
            EventBus.$emit('showAlert', {
              color: "error",
              type: "error",
              msg: body.popMessage
            });
          }
          break;
      }
    },
    saveMessage(action){
      return this.actionsToSave.includes(action);
    },
    getAdminIdAndName(){
      this.axios
          .get(`/v1/check/room-admin/${this.$route.params.roomId}`)
          .then((res) => {
            this.adminData.adminId = res.data.adminId;
            this.adminData.adminNickname = res.data.adminNickname;
            this.adminData.itsMe = this.isMyId(res.data.adminId);
            this.actionsToSave = this.adminData.itsMe?["MESSAGE","JOIN","LEAVE","SPAM"]:["MESSAGE","JOIN","LEAVE"];
          })
          .catch((e) => {
            EventBus.$emit('showAlert', {
              color: "error",
              type: "error",
              msg: `Error ${e.response.data.code}. ${e.response.data.message}`
            });
          });
    },
    timeFromDate(date){
      return dayjs(date).fromNow();
    },
    scrollChat() {
      const div = document.querySelector('.messagesContainer');
      if (div.scrollTop < div.scrollHeight - div.clientHeight) div.scrollTop += 100; // move down
    },
    messageColor(who){
      const spamColor = 'rgba(205, 10, 10, 0.6)';
      const joinColor = 'rgba(46,162,8,0.6)';
      const leaveColor = 'rgba(80, 80, 80, 0.6)';
      const myMessageColor = 'blue';
      const someoneMessageColor = 'grey';

      let color = "";

      if (who.action == 'SPAM') {
        color = spamColor;
      } else if (who.action == 'JOIN') {
        color = joinColor;
      } else if (who.action == 'LEAVE') {
        color = leaveColor;
      } else if (color != spamColor && this.isMyId(who.senderId)){
        color = myMessageColor;
      } else if (color != spamColor && !this.isMyId(who.senderId)) {
        color = someoneMessageColor;
      }

      return color;
    },
    whereGoesMessage(where){
      if (where.action == 'MESSAGE' && this.isMyId(where.senderId)) return 'ml-auto';
      return 'mr-auto';
    }
  }
}
</script>
<style>
.test {
  background-color:
}
</style>