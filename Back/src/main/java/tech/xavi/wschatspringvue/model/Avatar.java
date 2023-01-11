package tech.xavi.wschatspringvue.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import tech.xavi.wschatspringvue.configuration.exception.ChatError;
import tech.xavi.wschatspringvue.configuration.exception.ChatRuntimeException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public enum Avatar {

    ADVENTURER("adventurer"),
    ADVENTURER_NEUTRAL("adventurer-neutral"),
    BIG_EARS("big-ears"),
    BIG_EARS_NEUTRAL("big-ears-neutral"),
    BIG_SMILE("big-smile"),
    BOTTTS("bottts"),
    CROODLES("croodles"),
    CROODLES_NEUTRAL("croodles-neutral"),
    IDENTICON("identicon"),
    INITIALS("initials"),
    MICAH("micah"),
    MINIAVS("miniavs"),
    OPEN_PEEPS("open-peeps"),
    PERSONAS("personas"),
    PIXEL_ART("pixel-art"),
    PIXEL_ART_NEUTRAL("pixel-art-neutral");

    private final String url;

    public static List<String> availableAvatarsUrls(){
        List<String> avatarUrls = new ArrayList<>();
        for (Avatar av : Avatar.values()){
            avatarUrls.add(av.getUrl());
        };
        return avatarUrls;
    }

    public static Avatar convertAvatar(String avatarUrl){
        for (Avatar av : Avatar.values()){
            if (av.getUrl().equals(avatarUrl)) return av;
        };
        throw new ChatRuntimeException(ChatError.InvalidAvatar, HttpStatus.BAD_REQUEST);
    }

}
