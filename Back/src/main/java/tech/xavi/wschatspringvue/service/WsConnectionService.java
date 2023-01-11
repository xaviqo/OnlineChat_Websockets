package tech.xavi.wschatspringvue.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xavi.wschatspringvue.entity.ChatRoom;

@AllArgsConstructor
@Service
public class WsConnectionService {

    private final CommonRoomService commonRoomService;

    public boolean userIsWsInscribed(String userId, String roomId){
        ChatRoom chatRoom = commonRoomService.findChatRoomById(roomId);
        return chatRoom.getActiveUsers().contains(userId);
    }

    public boolean userIsInscribed(String userId, String roomId){
        ChatRoom chatRoom = commonRoomService.findChatRoomById(roomId);
        return chatRoom.getInscribedUsers().contains(userId);
    }

    public void saveActiveUser(String userId, String roomId){
        ChatRoom chatRoom = commonRoomService.findChatRoomById(roomId);
        chatRoom.setActiveUser(userId);
        commonRoomService.updateRoom(chatRoom);
    }

    public void removeActiveUser(String userId, String roomId){
        ChatRoom chatRoom = commonRoomService.findChatRoomById(roomId);
        chatRoom.removeActiveUser(userId);
        commonRoomService.updateRoom(chatRoom);
    }
}
