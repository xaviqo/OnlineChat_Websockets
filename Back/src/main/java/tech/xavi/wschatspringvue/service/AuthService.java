package tech.xavi.wschatspringvue.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.xavi.wschatspringvue.entity.RefreshToken;
import tech.xavi.wschatspringvue.model.TokenPayload;
import tech.xavi.wschatspringvue.repository.RefreshTokenRepository;
import tech.xavi.wschatspringvue.configuration.jwt.JwtHelper;
import tech.xavi.wschatspringvue.util.UUIDGenerator;

@AllArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public TokenPayload setAuthentication(String id, String password){

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(id, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUIDGenerator.randomUUID())
                .ownerId(authentication.getName())
                .build();

        refreshTokenRepository.save(refreshToken);

        return TokenPayload.builder()
                .accessToken(jwtHelper.generateAccessToken(id))
                .refreshToken(jwtHelper.generateRefreshToken(id,refreshToken))
                .build();
    }
}
