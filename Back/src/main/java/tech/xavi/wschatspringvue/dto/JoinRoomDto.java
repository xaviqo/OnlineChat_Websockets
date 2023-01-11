package tech.xavi.wschatspringvue.dto;

import lombok.Data;

@Data
public class JoinRoomDto {

    private String userNickname;
    private String avatarUrl;
    private String chatRoomId;
    private String chatRoomPassword;

}
