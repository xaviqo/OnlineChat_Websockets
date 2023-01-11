package tech.xavi.wschatspringvue.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.xavi.wschatspringvue.Globals;
import tech.xavi.wschatspringvue.configuration.exception.ChatError;
import tech.xavi.wschatspringvue.configuration.exception.ChatRuntimeException;
import tech.xavi.wschatspringvue.entity.ChatUser;
import tech.xavi.wschatspringvue.model.Avatar;
import tech.xavi.wschatspringvue.repository.UserRepository;
import tech.xavi.wschatspringvue.util.UUIDGenerator;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChatUser createUser(String nickname, String password, Avatar avatar){

        if (nickname.isBlank() || nickname.length() < Globals.NICKNAME_MIN_LEN)
            throw new ChatRuntimeException(ChatError.NicknameMinLength, HttpStatus.BAD_REQUEST);

        if (nickname.length() > Globals.NICKNAME_MAX_LEN)
            throw new ChatRuntimeException(ChatError.NicknameMaxLength,HttpStatus.BAD_REQUEST);

        if (!nickname.matches(Globals.REGEX_LETTERS_NUMBERS))
            throw new ChatRuntimeException(ChatError.NicknameOnlyAZ09,HttpStatus.BAD_REQUEST);

        return userRepository.save(ChatUser.builder()
                .username(nickname)
                .blockedIds(new HashSet<>())
                .password(passwordEncoder.encode(password))
                .id(UUIDGenerator.randomId())
                .avatar(avatar)
                .build());

    }

    public ChatUser findByUserId(String id){
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new ChatRuntimeException(ChatError.UserIdNotFound, HttpStatus.UNAUTHORIZED)
                );
    }

    public ChatUser getConnectedUserAuthentication(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return findByUserId(userDetails.getUsername());
    }
}
