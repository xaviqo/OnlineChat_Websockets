package tech.xavi.wschatspringvue.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.xavi.wschatspringvue.entity.RefreshToken;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
}
