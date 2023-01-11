package tech.xavi.wschatspringvue.util;

import java.util.UUID;

public class UUIDGenerator {
    public static String randomId(){
        final String uuid = randomUUID();
        return uuid.substring(uuid.length()-8).toLowerCase();
    }

    public static String randomPassword(){
        final String uuid = randomUUID();
        return uuid.substring(uuid.length()-16).toLowerCase();
    }

    public static String randomUUID(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
