package tech.xavi.wschatspringvue.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.xavi.wschatspringvue.Globals;
import tech.xavi.wschatspringvue.configuration.exception.ChatError;
import tech.xavi.wschatspringvue.configuration.exception.ChatRuntimeException;
import tech.xavi.wschatspringvue.dto.JoinRoomDto;
import tech.xavi.wschatspringvue.dto.NewRoomConfigurationDto;
import tech.xavi.wschatspringvue.dto.RoomCheckDto;
import tech.xavi.wschatspringvue.dto.UserLocalStorageDto;
import tech.xavi.wschatspringvue.entity.ChatRoom;
import tech.xavi.wschatspringvue.entity.ChatUser;
import tech.xavi.wschatspringvue.model.Avatar;
import tech.xavi.wschatspringvue.model.TokenPayload;
import tech.xavi.wschatspringvue.util.UUIDGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class RoomService {

    private final CommonRoomService commonRoomService;
    private final UserService userService;
    private final AuthService authService;


    public UserLocalStorageDto createNewRoom(final NewRoomConfigurationDto dto){

        if (!dto.getRoomTopic().matches(Globals.REGEX_LETTERS_NUMBERS))
            throw new ChatRuntimeException(ChatError.TopicOnlyAZ09,HttpStatus.BAD_REQUEST);

        if (dto.getUsersLimit() < 2)
            throw new ChatRuntimeException(ChatError.ChatUsersMinimum2,HttpStatus.BAD_REQUEST);

        if (dto.getUsersLimit() > Globals.MAX_ROOM_USERS)
            throw new ChatRuntimeException(ChatError.ChatUsersLimit,HttpStatus.BAD_REQUEST);

        final String adminPassword = UUIDGenerator.randomId();

        final ChatUser admin = userService.createUser(
                dto.getAdminNickname(),
                adminPassword,
                Avatar.convertAvatar(dto.getAvatarUrl())
        );

        final ChatRoom chatRoom = ChatRoom.builder()
                .id(UUIDGenerator.randomId())
                .roomTopic(dto.getRoomTopic())
                .adminId(admin.getId())
                .password(null)
                .usersLimit(dto.getUsersLimit())
                .showInLobby(dto.isShowInLobby())
                .lastActivity(LocalDateTime.now())
                .inscribedUsers(new HashSet<>())
                .activeUsers(new HashSet<>())
                .useSpamFilter(true)
                .build();

        if (dto.getRoomTopic().isBlank())
            chatRoom.setRoomTopic(Globals.ROOM_TOPIC_DEFAULT+chatRoom.getId().toUpperCase());

        if (dto.isPasswordProtected()) chatRoom.setPassword(UUIDGenerator.randomPassword());

        final TokenPayload tokenPayload = authService.setAuthentication(admin.getId(), adminPassword);

        chatRoom.joinRoom(admin);
        commonRoomService.updateRoom(chatRoom);

        WsSpamFilterService.createSpamFilter(chatRoom.getId());

        return UserLocalStorageDto
                .builder()
                .userId(admin.getId())
                .userNickname(admin.getUsername())
                .usersLimit(chatRoom.getUsersLimit())
                .tokenPayload(tokenPayload)
                .avatarUrl(dto.getAvatarUrl())
                .chatId(chatRoom.getId())
                .build();

    }

    public UserLocalStorageDto joinRoom(JoinRoomDto dto){

        final RoomCheckDto roomCheck = commonRoomService.checkRoomStatus(dto.getChatRoomId());

        if (roomCheck.isFull())
            throw new ChatRuntimeException(ChatError.RoomIsFull,HttpStatus.FORBIDDEN);

        final ChatRoom chatRoom = commonRoomService.findChatRoomById(dto.getChatRoomId());

        if (roomCheck.isHasPassword() && !dto.getChatRoomPassword().equalsIgnoreCase(chatRoom.getPassword())){
            throw new ChatRuntimeException(ChatError.IncorrectRoomPassword,HttpStatus.FORBIDDEN);
        }

        final String userPassword = UUIDGenerator.randomId();

        final ChatUser user = userService.createUser(
                dto.getUserNickname(),
                userPassword,
                Avatar.convertAvatar(dto.getAvatarUrl())
        );

        final TokenPayload tokenPayload = authService.setAuthentication(user.getId(), userPassword);

        chatRoom.joinRoom(user);

        commonRoomService.updateRoom(chatRoom);

        return UserLocalStorageDto
                .builder()
                .userId(user.getId())
                .userNickname(user.getUsername())
                .usersLimit(chatRoom.getUsersLimit())
                .tokenPayload(tokenPayload)
                .avatarUrl(dto.getAvatarUrl())
                .chatId(chatRoom.getId())
                .build();
    }



}
