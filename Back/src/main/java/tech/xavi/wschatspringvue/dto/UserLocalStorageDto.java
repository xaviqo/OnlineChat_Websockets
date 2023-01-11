package tech.xavi.wschatspringvue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.xavi.wschatspringvue.model.TokenPayload;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLocalStorageDto {
    private String id;
    private String userId;
    private String userNickname;
    private String roomTopic;
    private int usersLimit;
    private TokenPayload tokenPayload;
    private String avatarUrl;
    private String password;

}
