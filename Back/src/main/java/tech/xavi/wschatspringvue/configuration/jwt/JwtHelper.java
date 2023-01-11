package tech.xavi.wschatspringvue.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.xavi.wschatspringvue.Globals;
import tech.xavi.wschatspringvue.entity.RefreshToken;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class JwtHelper {

    private static final String issuer = "XaviTechChatWSSBV";

    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;
    private final Algorithm accessTokenAlgorithm;
    private final Algorithm refreshTokenAlgorithm;
    private final JWTVerifier accessTokenVerifier;
    private final JWTVerifier refreshTokenVerifier;


    public JwtHelper(){
        accessTokenExpirationMs = (long) Globals.ACCESS_TKN_EXP_MIN * 60 * 60 * 1000;
        refreshTokenExpirationMs = (long) Globals.REFRESH_TKN_EXP_DAY * 24 * 60 * 60 * 1000;
        accessTokenAlgorithm = Algorithm.HMAC256(UUID.randomUUID().toString());
        refreshTokenAlgorithm = Algorithm.HMAC256(UUID.randomUUID().toString());

        accessTokenVerifier = JWT.require(accessTokenAlgorithm)
                .withIssuer(issuer)
                .build();
        refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
                .withIssuer(issuer)
                .build();
    }

    public String generateAccessToken(String userId) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + accessTokenExpirationMs))
                .sign(accessTokenAlgorithm);
    }

    public String generateRefreshToken(String userId, RefreshToken refreshToken) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userId)
                .withClaim("tokenId", refreshToken.getId())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + refreshTokenExpirationMs))
                .sign(refreshTokenAlgorithm);
    }

    private Optional<DecodedJWT> decodeAccessToken(String token) {
        try {
            return Optional.of(accessTokenVerifier.verify(token));
        } catch (Exception e) {
            return null;
        }
    }

    private Optional<DecodedJWT> decodeRefreshToken(String token) {
        try {
            return Optional.of(refreshTokenVerifier.verify(token));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateAccessToken(String token) {
        return decodeAccessToken(token).isPresent();
    }

    public boolean validateRefreshToken(String token) {
        return decodeRefreshToken(token).isPresent();
    }

    public String getUserIdFromAccessToken(String token) {
        return decodeAccessToken(token).get().getSubject();
    }

    public String getUserIdFromRefreshToken(String token) {
        return decodeRefreshToken(token).get().getSubject();
    }

    public String getTokenIdFromRefreshToken(String token) {
        return decodeRefreshToken(token).get().getClaim("tokenId").asString();
    }
}
