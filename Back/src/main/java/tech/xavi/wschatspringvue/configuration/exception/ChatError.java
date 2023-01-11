package tech.xavi.wschatspringvue.configuration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.xavi.wschatspringvue.Globals;

@AllArgsConstructor
@Getter
public enum ChatError {

    NicknameMaxLength("001","Nicknames maximum length is "+Globals.NICKNAME_MAX_LEN+" characters"),
    NicknameMinLength("002","Nicknames minimum length is "+ Globals.NICKNAME_MIN_LEN+" characters"),
    NicknameOnlyAZ09("003","Nicknames can only contain letters or numbers"),
    TopicOnlyAZ09("003","Room names can only contain letters or numbers"),
    UserIdNotFound("005","User ID not found"),
    ChatUsersLimit("006","Rooms are limited to "+Globals.MAX_ROOM_USERS+" users"),
    ChatUsersMinimum2("007","The minimum room size is 2 users"),
    InvalidAvatar("008","The specified avatar is not available"),
    RoomIdNotFound("009","There is no chat room with this id")
    ;

    private final String code;
    private final String description;
}
