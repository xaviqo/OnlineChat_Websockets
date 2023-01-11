package tech.xavi.wschatspringvue.dto;

import lombok.Data;

@Data
public class NewRoomConfigurationDto {

    private String adminNickname;
    private String avatarUrl;
    private String roomTopic;
    private boolean passwordProtected;
    private boolean showInLobby;
    private int usersLimit;

}
