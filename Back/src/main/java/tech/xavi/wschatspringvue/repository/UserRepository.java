package tech.xavi.wschatspringvue.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.xavi.wschatspringvue.entity.ChatUser;

public interface UserRepository extends MongoRepository<ChatUser,String> {
}
