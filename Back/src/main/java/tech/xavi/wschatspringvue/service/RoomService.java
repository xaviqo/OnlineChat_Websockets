package tech.xavi.wschatspringvue.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.xavi.wschatspringvue.Globals;
import tech.xavi.wschatspringvue.dto.CreatedRoomConfigurationDto;
import tech.xavi.wschatspringvue.dto.NewRoomConfigurationDto;
import tech.xavi.wschatspringvue.dto.RoomCheckDto;
import tech.xavi.wschatspringvue.entity.ChatRoom;
import tech.xavi.wschatspringvue.entity.ChatUser;
import tech.xavi.wschatspringvue.model.Avatar;
import tech.xavi.wschatspringvue.model.TokenPayload;
import tech.xavi.wschatspringvue.repository.RoomRepository;
import tech.xavi.wschatspringvue.configuration.exception.ChatError;
import tech.xavi.wschatspringvue.configuration.exception.ChatRuntimeException;
import tech.xavi.wschatspringvue.util.UUIDGenerator;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserService userService;
    private final AuthService authService;

    public CreatedRoomConfigurationDto createNewRoom(final NewRoomConfigurationDto dto){

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
                .admin(admin)
                .password(null)
                .usersLimit(dto.getUsersLimit())
                .activeUsers(0)
                .showInLobby(dto.isShowInLobby())
                .lastActivity(LocalDateTime.now())
                .build();

        if (dto.getRoomTopic().isBlank())
            chatRoom.setRoomTopic(Globals.ROOM_TOPIC_DEFAULT+chatRoom.getId().toUpperCase());

        if (dto.isPasswordProtected()) chatRoom.setPassword(UUIDGenerator.randomPassword());

        roomRepository.save(chatRoom);

        final TokenPayload tokenPayload = authService.setAuthentication(admin.getId(), adminPassword);

        return CreatedRoomConfigurationDto
                .builder()
                .id(chatRoom.getId())
                .userId(admin.getId())
                .userNickname(dto.getAdminNickname())
                .roomTopic(chatRoom.getRoomTopic())
                .usersLimit(chatRoom.getUsersLimit())
                .password(chatRoom.getPassword())
                .tokenPayload(tokenPayload)
                .avatarUrl(dto.getAvatarUrl())
                .build();

    }

    public RoomCheckDto checkRoom(String roomId){
        ChatRoom room = findChatRoomById(roomId);

        return RoomCheckDto.builder()
                .hasPassword(Objects.nonNull(room.getPassword()))
                .isFull(room.getUsersLimit() == room.getActiveUsers())
                .build();
    }

    public ChatRoom findChatRoomById(String roomId){
        return roomRepository.findById(roomId)
                .orElseThrow(
                        () -> new ChatRuntimeException(ChatError.RoomIdNotFound,HttpStatus.NOT_FOUND)
                );

    }

}
