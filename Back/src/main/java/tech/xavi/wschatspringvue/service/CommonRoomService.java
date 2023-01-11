package tech.xavi.wschatspringvue.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.xavi.wschatspringvue.configuration.exception.ChatError;
import tech.xavi.wschatspringvue.configuration.exception.ChatRuntimeException;
import tech.xavi.wschatspringvue.dto.RoomAdminDto;
import tech.xavi.wschatspringvue.dto.RoomCheckDto;
import tech.xavi.wschatspringvue.entity.ChatRoom;
import tech.xavi.wschatspringvue.entity.ChatUser;
import tech.xavi.wschatspringvue.repository.RoomRepository;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CommonRoomService {

    private final RoomRepository roomRepository;
    private final UserService userService;

    public RoomCheckDto checkRoomStatus(String roomId){
        final ChatRoom room = findChatRoomById(roomId);
        return RoomCheckDto.builder()
                .hasPassword(Objects.nonNull(room.getPassword()))
                .isFull(room.isFull())
                .build();
    }

    public ChatRoom findChatRoomById(String roomId){
        return roomRepository.findById(roomId)
                .orElseThrow(
                        () -> new ChatRuntimeException(
                                ChatError.RoomIdNotFound,
                                HttpStatus.NOT_FOUND
                        ));

    }

    public void updateRoom(ChatRoom chatRoom){
        roomRepository.save(chatRoom);
    }

    public boolean userIsInscribed(String roomId){
        ChatUser user = userService.getConnectedUserAuthentication();
        ChatRoom room = findChatRoomById(roomId);
        if (!room.isUserInscribed(user)) throw new ChatRuntimeException(ChatError.UserNotInscribed,HttpStatus.FORBIDDEN);
        return true;
    }

    public RoomAdminDto whoIsAdmin(String roomId){
        ChatUser user = userService.getConnectedUserAuthentication();
        ChatRoom room = findChatRoomById(roomId);
        if (!room.isUserInscribed(user)) throw new ChatRuntimeException(ChatError.UserNotInscribed,HttpStatus.FORBIDDEN);
        String adminNickname = userService.findByUserId(room.getAdminId()).getUsername();
        return RoomAdminDto
                .builder()
                .adminId(room.getAdminId())
                .adminNickname(adminNickname)
                .build();
    }
}
