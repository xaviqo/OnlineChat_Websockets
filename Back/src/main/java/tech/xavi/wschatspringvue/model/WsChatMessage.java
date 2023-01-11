package tech.xavi.wschatspringvue.model;

import lombok.Data;

@Data
public class WsChatMessage {

    private String senderId;
    private String senderName;
    private String message;
}
