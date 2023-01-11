package tech.xavi.wschatspringvue.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.xavi.wschatspringvue.entity.ChatRoom;

public interface RoomRepository extends MongoRepository<ChatRoom, String> {

}
