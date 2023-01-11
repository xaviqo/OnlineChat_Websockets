package tech.xavi.wschatspringvue.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tech.xavi.wschatspringvue.Globals;
import tech.xavi.wschatspringvue.model.WsAction;
import tech.xavi.wschatspringvue.model.WsPayload;

import java.time.LocalDateTime;

@AllArgsConstructor
@Slf4j
@Service
public class WsChatService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessage(String roomId, WsPayload message) {

        final String sendLocation = getSendLocation(roomId);
        final WsPayload payload = WsSpamFilterService.filterSpam(roomId,message);
        payload.setPopMessage(getPopMessage(message.getAction(),message.getSenderName()));
        payload.setTime(LocalDateTime.now().toString());
        //payload.setTime(TimeUtil.stringifyLTN(LocalTime.now()));

        simpMessagingTemplate.convertAndSend(sendLocation,payload);
    }


    public void sendActivityMessage(String roomId, String senderName, String senderId, WsAction action) {

        final String popMessage = getPopMessage(action,senderName);
        final String sendLocation = getSendLocation(roomId);

        simpMessagingTemplate.convertAndSend(sendLocation,
                WsPayload.builder()
                        .senderId(senderId)
                        .senderName(senderName)
                        .popMessage(popMessage)
                        .action(action)
                        .time(LocalDateTime.now().toString())
                        .build()
        );
    }

    private String getSendLocation(String lobbyId) {
        return Globals.WS_LOBBY_TOPIC + lobbyId;
    }

    private String getPopMessage(WsAction action, String senderName) {
        return switch (action) {
            case MESSAGE -> WsAction.MESSAGE.getMessage();
            case SPAM -> senderName + WsAction.SPAM.getMessage();
            case JOIN -> senderName + WsAction.JOIN.getMessage();
            case LEAVE -> senderName + WsAction.LEAVE.getMessage();
            case KICK -> senderName + WsAction.KICK.getMessage();
            case BAN -> senderName + WsAction.BAN.getMessage();
            case DELETE -> WsAction.DELETE.getMessage();
        };
    }


}
