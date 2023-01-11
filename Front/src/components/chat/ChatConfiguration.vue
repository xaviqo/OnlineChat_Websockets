<template>
  <v-card height="90vh" tile elevation="0">
    <v-list>
      <v-list-item three-line>
          <v-badge
              bordered
              bottom
              color="green"
              dot
              offset-x="13"
              offset-y="13"
          >
            <v-avatar size="60" elevation="10">
              <img src="https://cdn.vuetifyjs.com/images/lists/1.jpg" />
            </v-avatar>
          </v-badge>
          <div class="ml-2">
            <v-card-title class="justify-center">Fernando Gaucho</v-card-title>
            <v-card-subtitle class="justify-center">Chat Administrator</v-card-subtitle>
          </div>
      </v-list-item>
        <v-divider></v-divider>
      <v-list-item two-line v-for="setting in settingButtons" :key="setting.option">
        <v-btn
            small
            block
            tile
            elevation="0"
            @click="changeSetting(setting.option)"
        >
          {{ setting.text }}
        </v-btn>
      </v-list-item>
    </v-list>
    <!--
      Ver enlace compartir

    -->
  </v-card>
</template>

<script>
export default {
  name: "ChatConfiguration",
  data: () => ({
    settingButtons: [
      { text: 'Users Limit', option: 1 },
      { text: 'Room Topic', option: 2 },
      { text: 'Password', option: 3 },
      { text: 'Spam Filter', option: 4 },
      { text: 'Delete Room', option: 5 }
    ]
  }),
  methods: {
    changeSetting(option){
      console.log(option)
    }
  }
}
</script>

<style scoped>
.column-category {
  background-color: grey;
}
</style>