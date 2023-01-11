<template>
  <v-card height="90vh" tile elevation="0">
    <v-list subheader>
      <v-subheader>Chat users</v-subheader>


      <v-list-item
          v-for="chat in recent"
          :key="chat.title"
          two-line
      >
        <v-badge
            bordered
            bottom
            color="green"
            dot
            overlap
            offset-x="25"
            offset-y="25"
        >
        <v-list-item-avatar>
          <v-img
              :alt="`${chat.title} avatar`"
              :src="chat.avatar"
          ></v-img>
        </v-list-item-avatar>
        </v-badge>
        <v-list-item-content>
          <v-list-item-title v-text="chat.title"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-icon>
          <v-btn icon class="mb-n2" plain>
            <v-icon color="error" >
              mdi-close-circle-outline
            </v-icon>
          </v-btn>
          <v-btn icon class="mb-n2" plain>
            <v-icon :color="chat.active ? 'deep-purple accent-4' : 'grey'">
              mdi-message-outline
            </v-icon>
          </v-btn>
        </v-list-item-icon>
      </v-list-item>
    </v-list>

    <v-divider></v-divider>
  </v-card>
</template>

<script>
export default {
  name: 'ChatUsers',
  data: () => ({
    recent: [
      {
        active: true,
        avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
        title: 'Jason Oner',
      },
      {
        active: true,
        avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
        title: 'Mike Carlson',
      },
      {
        avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
        title: 'Cindy Baker',
      },
      {
        avatar: 'https://cdn.vuetifyjs.com/images/lists/4.jpg',
        title: 'Ali Connors',
      },
    ]
  }),
}
</script>

<style scoped>

</style>