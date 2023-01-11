<template>
  <v-card
      tile
      dark
      elevation="0"
      class="full-height"
  >
    <v-img
        height="55vh"
        src="https://unsplash.com/photos/9OKGEVJiTKk/download?force=true&w=640"
        class="pa-12"
    >
      <v-row fluid class="fill-height d-flex justify-center align-center" v-if="showCard">
        <v-col cols="10">

          <v-card light class="pa-6 transparent-title-color" transition="fade-transition">
            <v-form
                ref="refCreateRoom"
                v-model="validCreateRoomForm"
                lazy-validation
            >
              <v-row>
                <v-col cols="6">
                  <v-card-title class="ml-n4">
                    <v-icon class="mr-5">mdi-tune-variant</v-icon>
                    Customize your chat room
                  </v-card-title>
                </v-col>
                <v-col cols="6" class="text-right">
                  <v-btn icon @click="showCard = false"><v-icon>mdi-close</v-icon></v-btn>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      v-model="roomDto.roomTopic"
                      label="Room name/topic (or leave empty)"
                      filled
                  ></v-text-field>
                </v-col>
                <v-col cols="4">
                  <v-text-field
                      v-model="roomDto.adminNickname"
                      :rules="rules.name"
                      label="Your nickname"
                      required
                      filled
                  ></v-text-field>
                </v-col>
                <v-col cols="1" class="ml-n1">
                  <v-avatar
                    color="grey"
                    class="lighten-2"
                    size="50">
                    <v-icon v-if="roomDto.adminNickname.length <= 3" x-large>mdi-help-circle-outline</v-icon>
                    <v-img height="50" :src="avatarizeNickname(roomDto.adminNickname)" v-if="roomDto.adminNickname.length > 3"></v-img>
                  </v-avatar>
                </v-col>
                <v-col cols="1">
                  <v-avatar
                      color="transparent"
                      size="50">
                    <v-icon @click="newAvatarStyle(userAvatarStyle)">mdi-dice-5-outline</v-icon>
                  </v-avatar>
                </v-col>
                <v-col cols="6">
                  <v-select
                      v-model="roomDto.usersLimit"
                      :items="this.maxUsers"
                      :rules="[v => !!v || 'Item is required']"
                      label="Users limit"
                      required
                      filled
                  ></v-select>
                </v-col>
                <v-col cols="3">
                  <v-checkbox
                      v-model="roomDto.passwordProtected"
                      label="Password protected?"
                      filled
                      class="mt-n1"
                  ></v-checkbox>
                </v-col>
                <v-col cols="3">
                  <v-checkbox
                      v-model="roomDto.showInLobby"
                      label="Show in lobby explorer?"
                      filled
                      class="mt-n1"
                  ></v-checkbox>
                </v-col>
              </v-row>

            </v-form>
          </v-card>
        </v-col>

        <v-row fluid class="d-flex justify-center align-center mt-n12">
            <v-btn class="transparent-title-color" light x-large outlined elevation="8">
              <v-card-title class="full-height justify-center" primary-title
              @click="createRoom"
              >
                Create chat room
              </v-card-title>
            </v-btn>
        </v-row>

      </v-row>

      <v-row fluid class="fill-height" v-if="!showCard">
        <v-col class="d-flex justify-center align-center">
          <v-btn class="transparent-title-color" light x-large outlined elevation="8" @click="showCard = true">
            <v-card-title primary-title>
              Create and customize your own chat room
            </v-card-title>
          </v-btn>
        </v-col>
      </v-row>

    </v-img>
  </v-card>
</template>

<script>
import { EventBus } from '@/main';
import { mixins } from '@/mixins';
export default {
  mixins: [mixins],
  name: 'CreateRoomCard',
  created() {
    this.getMaxUsers();
    this.getAvatarStyles();
  },
  data: () => ({
    validCreateRoomForm: false,
    showCard: false,
    maxUsersLimit: 0,
    maxUsers: [],
    avatarStyles: [],
    userAvatarStyle: '',
    roomDto: {
      roomTopic: "",
      adminNickname: "",
      passwordProtected: false,
      showInLobby: false,
      usersLimit: 2,
      avatarUrl: '',
    },
    rules: {
      name: [
        v => !!v || 'Nickname is required',
        v => !(v && v.length > 14) || 'Nickname must be less than 14 characters',
        v => !(v && v.length < 4) || 'Nickname must be at least 4 characters',
      ],
      limit: [
        v => !!v || 'Limit is required',
        v => !(v && v < 2) || "The minimum room size is 2 users",
        v => !(v && v > this.maxUsersLimit) || "Rooms are limited to "+this.maxUsersLimit+" users",
      ]
    }
  }),
  methods: {
    createRoom(){

      if (this.$refs.refCreateRoom.validate()){

        this.roomDto.avatarUrl = this.userAvatarStyle;

        this.axios
            .post(`/v1/room/create`,this.roomDto)
            .then((res) => {
              this.$refs.refCreateRoom.reset();
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
              console.log(e)
              EventBus.$emit('showAlert', {
                color: "error",
                type: "error",
                msg: `Error ${e.response.data.code}. ${e.response.data.message}`
              });
            });
      } else {
        EventBus.$emit('showAlert', {
          color: "error",
          type: "error",
          msg: `Form is not valid`
        });
      }

    },
    getMaxUsers(){
      this.axios
          .get(`/v1/cfg/users-limit`)
          .then((res) => {
            this.maxUsersLimit = res.data;
            for (let i = 2; i <= this.maxUsersLimit; i++) {
              this.maxUsers.push(i);
            }
          });

    },
    getAvatarStyles(){
      this.axios
          .get(`/v1/cfg/avatar-styles`)
          .then((res) => {
            this.avatarStyles = res.data;
            this.newAvatarStyle('');
          });
    },
    avatarizeNickname(nickname){
      return `https://avatars.dicebear.com/api/${this.userAvatarStyle}/${nickname}.svg`
    },
    newAvatarStyle(actualStyle){
      const totalStyles = this.avatarStyles.length;
      const newStyle = this.avatarStyles[Math.floor(totalStyles * Math.random())];
      if (newStyle === actualStyle) this.newAvatarStyle(actualStyle);
      else this.userAvatarStyle = newStyle;
    }
  },
}
</script>

<style scoped>
.transparent-title-color {
  background-color: rgba(255, 255, 255, .8) !important;
  border-color: white !important;
}
</style>