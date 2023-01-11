package tech.xavi.wschatspringvue.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xavi.wschatspringvue.model.WsAction;
import tech.xavi.wschatspringvue.model.WsPayload;
import tech.xavi.wschatspringvue.model.WsSpamType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class WsSpamFilterService {

    private static final Map<String,SpamFilter> spamFilters = new HashMap<>();

    public static WsPayload filterSpam(String roomId, WsPayload message){

        boolean isSpam = false;

        if (userReachedPointsLimit(message.getSenderId(),roomId)){
            isSpam = true;
            message.setSpamType(WsSpamType.LIMIT.getType());
        }

        if (!isSpam && containsBannedWords(message.getMessage(),message.getSenderId(),roomId)){
            isSpam = true;
            message.setSpamType(WsSpamType.BANNED_WORD.getType());
        }

        if (!isSpam && isMessageTooLong(message.getMessage(),message.getSenderId(),roomId)){
            isSpam = true;
            message.setSpamType(WsSpamType.MSG_TOO_LONG.getType());
        }

        if (!isSpam && isSameMessageAsPreviousOne(message.getMessage(),message.getSenderId(),roomId)){
            isSpam = true;
            message.setSpamType(WsSpamType.REPEATED_MSG.getType());
        }

        if (!isSpam && NotEnoughTimeBetweenMessages(message.getSenderId(), roomId)){
            isSpam = true;
            message.setSpamType(WsSpamType.TIME_BTWN_MSG.getType());
        }

        if (!isSpam && messageHasUrls(message.getMessage(), message.getSenderId(), roomId)){
            isSpam = true;
            message.setSpamType(WsSpamType.URL_SENT.getType());
        }

        if (isSpam) message.setAction(WsAction.SPAM);

        return message;
    }

    public static boolean containsBannedWords(String userMessage, String userId, String roomId){
        SpamFilter spamFilter = spamFilters.get(roomId);
        if (spamFilter == null) return false;
        return spamFilter.hasBannedWords(userId,userMessage);
    }

    public static boolean userReachedPointsLimit(String userId, String roomId){
        SpamFilter spamFilter = spamFilters.get(roomId);
        if (spamFilter == null) return false;
        return spamFilter.userReachedMaxSpamPoints(userId);
    }

    public static boolean isMessageTooLong(String userMessage, String userId, String roomId){
        SpamFilter spamFilter = spamFilters.get(roomId);
        if (spamFilter == null) return false;
        return spamFilter.isMessageTooLong(userId,userMessage);
    }

    public static boolean isSameMessageAsPreviousOne(String userMessage, String userId, String roomId){
        SpamFilter spamFilter = spamFilters.get(roomId);
        if (spamFilter == null) return false;
        return !spamFilter.isMessageDifferentFromPreviousOne(userId,userMessage);
    }

    public static boolean NotEnoughTimeBetweenMessages(String userId, String roomId){
        SpamFilter spamFilter = spamFilters.get(roomId);
        if (spamFilter == null) return false;
        return !spamFilter.enoughTimeBetweenMsg(userId, LocalDateTime.now());
    }

    public static boolean messageHasUrls(String userMessage, String userId, String roomId){
        SpamFilter spamFilter = spamFilters.get(roomId);
        if (spamFilter == null) return false;
        return spamFilter.containsUrl(userId,userMessage);
    }

    public static void saveUser(String userId, String roomId){
        SpamFilter spamFilter = spamFilters.get(roomId);
        if (spamFilter != null && !spamFilter.containsUser(userId)) {
            spamFilter.saveNewUser(userId);
        }
    }

    public static void removeUser(String userId, String roomId){
        SpamFilter spamFilter = spamFilters.get(roomId);
        if (spamFilter != null && spamFilter.containsUser(userId)){
            spamFilter.removeUser(userId);
        }
    }
    public static void createSpamFilter(String roomId){
        spamFilters.put(roomId,new SpamFilter());
    }

    public static void removeSpamFilter(String roomId){
        spamFilters.remove(roomId);
    }
}
