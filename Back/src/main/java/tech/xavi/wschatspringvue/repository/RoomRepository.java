package tech.xavi.wschatspringvue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.xavi.wschatspringvue.entity.ChatRoom;

public interface RoomRepository extends JpaRepository<ChatRoom,String> {

}
