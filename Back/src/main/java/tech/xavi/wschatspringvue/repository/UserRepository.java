package tech.xavi.wschatspringvue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.xavi.wschatspringvue.entity.ChatUser;

public interface UserRepository extends JpaRepository<ChatUser,String> {
}
