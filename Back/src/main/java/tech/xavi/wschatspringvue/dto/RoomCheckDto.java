package tech.xavi.wschatspringvue.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomCheckDto {
    private boolean hasPassword;
    private boolean isFull;
}
