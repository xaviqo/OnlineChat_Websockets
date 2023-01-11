package tech.xavi.wschatspringvue.service;

import lombok.Data;
import tech.xavi.wschatspringvue.Globals;
import tech.xavi.wschatspringvue.configuration.spamfilter.SpamFilterDefaultCfg;

import java.time.LocalDateTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.SECONDS;

@Data
public class SpamFilter {

    private int maxMessageLength;
    private Set<String> bannedWords;
    private int spamPoints;
    private int secBetweenMsg;
    private boolean allowUrls;
    private Map<String, LocalDateTime> timeFromLastMsgControl;
    private Map<String,String> lastSentMsgControl;
    private Map<String,Integer> spamPointsControl;

    public SpamFilter() {
        this.maxMessageLength = SpamFilterDefaultCfg.getMaxMessageLength_default();
        this.bannedWords = new HashSet<>(Arrays.asList(SpamFilterDefaultCfg.getBannedWords_default()));
        this.spamPoints = SpamFilterDefaultCfg.getSpamPoints_default();
        this.secBetweenMsg = SpamFilterDefaultCfg.getSecBetweenMessages_default();
        this.allowUrls = SpamFilterDefaultCfg.isAllowUrls_default();
        this.timeFromLastMsgControl = new HashMap<>();
        this.lastSentMsgControl = new HashMap<>();
        this.spamPointsControl = new HashMap<>();
    }

    public boolean hasBannedWords(String userId, String message){
        for (String bannedWord : getBannedWords()){
            if (message.toUpperCase().contains(bannedWord.toUpperCase())){
                increaseSpamPoints(userId);
                return true;
            }
        }
        return false;
    }

    public boolean isMessageTooLong(String userId, String message){
        if (message.length() > getMaxMessageLength()){
            increaseSpamPoints(userId);
            return true;
        }
        return false;
    }

    public boolean enoughTimeBetweenMsg(String userId, LocalDateTime newMsgTime){
        final LocalDateTime lastMsgTime = timeFromLastMsgControl.get(userId);
        if (lastMsgTime != null){
            final long diff = SECONDS.between(lastMsgTime,newMsgTime);
            if (diff <= getSecBetweenMsg()){
                increaseSpamPoints(userId);
                return false;
            }
            timeFromLastMsgControl.put(userId,newMsgTime);
        }
        return true;
    }

    public boolean isMessageDifferentFromPreviousOne(String userId, String newMessage){
        if (!newMessage.isBlank()){
            if (!lastSentMsgControl.get(userId)
                    .equalsIgnoreCase(newMessage)){
                lastSentMsgControl.put(userId,newMessage);
                return true;
            }
        }
        increaseSpamPoints(userId);
        return false;
    }

    public boolean containsUrl(String userId, String message){
        if (isAllowUrls()) return false;
        for (String urlParts : Globals.WS_HTTP_FILTER){
            if (message.contains(urlParts)) {
                increaseSpamPoints(userId);
                return true;
            }
        }
        return false;
    }

    public boolean userReachedMaxSpamPoints(String userId){
        return (spamPointsControl.get(userId) >= getSpamPoints());
    }

    private void increaseSpamPoints(String userId){
        spamPointsControl.put(userId,spamPointsControl.get(userId)+1);
    }

    public boolean containsUser(String userId){
        return timeFromLastMsgControl.containsKey(userId);
    }

    public void saveNewUser(String userId){
        timeFromLastMsgControl.put(userId,LocalDateTime.now().minusSeconds(getSecBetweenMsg()-1));
        lastSentMsgControl.put(userId,UUID.randomUUID().toString());
        spamPointsControl.put(userId,0);
    }

    public void removeUser(String userId){
        timeFromLastMsgControl.remove(userId);
        lastSentMsgControl.remove(userId);
        spamPointsControl.remove(userId);
    }
}
