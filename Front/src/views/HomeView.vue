<template>
  <div class="home">
    <CaruselHome></CaruselHome>
    <v-row>
      <v-col class="ma-0 pa-0 ml-4 mt-4 mr-1">
        <CreateRoomCard></CreateRoomCard>
      </v-col>
      <v-col class="ma-0 pa-0 mr-4 mt-4 ml-1">
        <PublicLobbiesCard></PublicLobbiesCard>
      </v-col>
    </v-row>
    <ExploreLobbiesDialog></ExploreLobbiesDialog>
  </div>
</template>
<script>
import CaruselHome from "@/components/home/CaruselHome.vue";
import CreateRoomCard from "@/components/home/CreateRoomCard.vue";
import PublicLobbiesCard from "@/components/home/PublicLobbiesCard.vue";
import ExploreLobbiesDialog from "@/components/dialog/ExploreLobbiesDialog.vue";
  export default {
    name: 'Home',
    components: {
      CaruselHome,
      CreateRoomCard,
      PublicLobbiesCard,
      ExploreLobbiesDialog
    },
  }
</script>
<style>
</style>
