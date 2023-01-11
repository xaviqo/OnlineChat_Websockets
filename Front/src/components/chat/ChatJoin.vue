<template>
    <v-row justify="center">
      <v-dialog
          v-model="showDialog"
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
            <v-col v-if="roomCfg.hasPassword" cols="5">
              <v-text-field
                  prepend-inner-icon="mdi-lock"
                  v-model="joinDto.password"
                  :rules="rules.password"
                  label="Room password"
                  required
                  filled
              ></v-text-field>
            </v-col>
            <v-col>
              <v-text-field
                  prepend-inner-icon="mdi-account"
                  v-model="joinDto.userNickname"
                  :rules="rules.name"
                  label="Your nickname"
                  required
                  filled
              ></v-text-field>
            </v-col>
            <v-col cols="2">
              <v-avatar
                  color="grey"
                  class="lighten-2"
                  size="50">
                <v-icon v-if="joinDto.userNickname.length <= 3" x-large>mdi-help-circle-outline</v-icon>
                <v-img height="50" :src="avatarizeNickname(joinDto.userNickname,joinDto.avatarUrl)" v-if="joinDto.userNickname.length > 3"></v-img>
              </v-avatar>
              <v-avatar
                  color="transparent"
                  size="50">
                <v-icon @click="newAvatarStyle(joinDto.avatarUrl)">mdi-dice-5-outline</v-icon>
              </v-avatar>
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
                @click="joinRoom()"
                v-if="!roomCfg.full"
            >
              Join
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
</template>

<script>
import {EventBus} from "@/main";
import {mixins} from "@/mixins";

export default {
  mixins: [mixins],
  name: "ChatJoin",
  data: () => ({
    showDialog: true,
    roomCfg: {
      hasPassword: false,
      full: false
    },
    joinDto: {
      userNickname: '',
      avatarUrl: '',
      chatRoomPassword: '',
      chatRoomId: ''
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
    avatarStyles: []
  }),
  created() {
    this.loadRoomCfg();
    if (this.isSessionSetted()) this.isUserAlreadyInscribed();
    this.loadNotRegisteredUserData();
  },
  methods: {
    joinRoom(){
      this.axios
          .post(`/v1/room/join`,this.joinDto)
          .then((res) => {
            this.showDialog = false;
            if (res.status === 201) {
              EventBus.$emit('showAlert', {
                color: "success",
                type: "success",
                msg: `Chat room successfully created`
              });
              if (this.isSessionSetted()) this.removeSession();
              this.saveSession(res.data);
              setTimeout(
                  this.$router.push(`/room/${this.getChatId()}`)
                  ,1000);
            }
          })
          .catch((e) => {
            EventBus.$emit('showAlert', {
              color: "error",
              type: "error",
              msg: `Error ${e.response.data.code}. ${e.response.data.message}`
            });
          });
    },
    loadNotRegisteredUserData(){
      this.getAvatarStyles();
      this.joinDto.chatRoomId = this.$route.params.roomId;
    },
    getAvatarStyles(){
      this.axios
          .get(`/v1/cfg/avatar-styles`)
          .then((res) => {
            this.avatarStyles = res.data;
            this.newAvatarStyle('');
          });
    },
    newAvatarStyle(actualStyle){
      const totalStyles = this.avatarStyles.length;
      const newStyle = this.avatarStyles[Math.floor(totalStyles * Math.random())];
      if (newStyle === actualStyle) this.newAvatarStyle(actualStyle);
      else this.joinDto.avatarUrl = newStyle;
    },
    loadRoomCfg(){
      this.axios
          .get(`/v1/check/room-status/${this.$route.params.roomId}`,)
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
            this.showDialog = false;
            this.$router.push(`/404`);
          });
    },
    isUserAlreadyInscribed(){
      this.axios
          .get(`/v1/check/user-inscribed/${this.$route.params.roomId}`)
          .then((res) => {
            this.showDialog = false;
            this.$router.push("/room/"+this.$route.params.roomId);
          })
    },
  }
}
</script>

<style scoped>

</style>