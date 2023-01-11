package tech.xavi.wschatspringvue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.xavi.wschatspringvue.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
