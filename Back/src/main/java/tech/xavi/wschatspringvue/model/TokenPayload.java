package tech.xavi.wschatspringvue.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPayload {

    String accessToken;
    String refreshToken;

}