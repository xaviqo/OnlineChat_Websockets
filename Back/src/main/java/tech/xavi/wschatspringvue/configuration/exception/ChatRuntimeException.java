package tech.xavi.wschatspringvue.configuration.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

public class ChatRuntimeException extends RuntimeException{

    private final ChatError error;
    private final HttpStatus httpStatus;

    public ChatRuntimeException(ChatError error, HttpStatus httpStatus){
        super();
        this.error = error;
        this.httpStatus = httpStatus;
    }

}
