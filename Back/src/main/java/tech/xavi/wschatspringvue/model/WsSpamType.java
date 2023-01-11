package tech.xavi.wschatspringvue.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WsSpamType {
    LIMIT("has exceeded the spam limit and can no longer send messages","Spam limit reached"),
    BANNED_WORD("is using banned words","Using banned word"),
    MSG_TOO_LONG("has exceeded the limit of characters per message","Message size limit exceeded"),
    REPEATED_MSG("is repeating same message in the chat","Repeated message"),
    TIME_BTWN_MSG("should leave more time between chat messages","Not enough time between messages"),
    URL_SENT("is sending URLs","Sending URLs")
    ;
    private String message;
    private String type;
}
