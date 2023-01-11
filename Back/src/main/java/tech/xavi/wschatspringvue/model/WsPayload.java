package tech.xavi.wschatspringvue.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WsPayload {
    private String senderId;
    private String senderName;
    private WsAction action;
    private String message;
    private String popMessage;
    private String time;
    private String spamType;

}
