package tech.xavi.wschatspringvue.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomAdminDto {

    String adminId;
    String adminNickname;

}
