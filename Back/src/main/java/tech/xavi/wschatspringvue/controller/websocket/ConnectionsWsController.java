package tech.xavi.wschatspringvue.controller.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@AllArgsConstructor
@Component
public class ConnectionsWsController {

    @EventListener
    public void handleConnect(SessionConnectEvent event) {


    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {


    }
}
