package tech.xavi.wschatspringvue.controller.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tech.xavi.wschatspringvue.configuration.jwt.JwtHelper;
import tech.xavi.wschatspringvue.model.WsAction;
import tech.xavi.wschatspringvue.service.WsChatService;
import tech.xavi.wschatspringvue.service.WsConnectionService;
import tech.xavi.wschatspringvue.service.WsSpamFilterService;

@Slf4j
@AllArgsConstructor
@Component
public class ConnectionsWsController {

    private final WsConnectionService wsConnectionService;
    private final WsChatService wsChatService;
    private final JwtHelper jwtHelper;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        final String userId = accessor.getNativeHeader("senderId").get(0);
        final String token = accessor.getNativeHeader("token").get(0);
        final String nickname = accessor.getNativeHeader("senderName").get(0);
        final String roomId = accessor.getNativeHeader("roomId").get(0);

        if (jwtHelper.getUserIdFromAccessToken(token).equals(userId)
                && wsConnectionService.userIsInscribed(userId,roomId)) {

            wsConnectionService.saveActiveUser(userId,roomId);
            WsSpamFilterService.saveUser(userId,roomId);
            wsChatService.sendActivityMessage(roomId,nickname,userId,WsAction.JOIN);
        }

    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {

        final String[] sessionId = event.getSessionId().split(":");
        final String roomId = sessionId[0];
        final String userId = sessionId[1];
        final String nickname = sessionId[2];

        wsConnectionService.removeActiveUser(userId,roomId);
        WsSpamFilterService.removeUser(userId,roomId);
        wsChatService.sendActivityMessage(roomId,nickname,userId, WsAction.LEAVE);

    }
}
