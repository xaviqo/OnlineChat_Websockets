package tech.xavi.wschatspringvue.controller.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tech.xavi.wschatspringvue.model.WsAction;
import tech.xavi.wschatspringvue.model.WsPayload;
import tech.xavi.wschatspringvue.service.WsChatService;
import tech.xavi.wschatspringvue.service.WsConnectionService;

@AllArgsConstructor
@Controller
@Slf4j
public class MessageWsController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final WsConnectionService wsConnectionService;
    private final WsChatService wsChatService;

    @MessageMapping("/chat/{lobby}")
    public void lobbyChatRoom(@DestinationVariable String lobby, WsPayload message) {
        if (wsConnectionService.userIsWsInscribed(message.getSenderId(),lobby)) {
            message.setAction(WsAction.MESSAGE);
            wsChatService.sendMessage(lobby, message);
        }
    }
}
