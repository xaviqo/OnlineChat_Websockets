package tech.xavi.wschatspringvue.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    private String id;
    private String roomTopic;
    private String adminId;
    private String password;
    private int usersLimit;
    private boolean showInLobby;
    private boolean useSpamFilter;
    private LocalDateTime lastActivity;
    Set<String> inscribedUsers;
    Set<String> activeUsers;

    public void joinRoom(ChatUser user){
        if (!isFull()) inscribedUsers.add(user.getId());
    }

    public void setActiveUser(String userId){
        activeUsers.add(userId);
    }

    public void removeActiveUser(String userId){
        activeUsers.remove(userId);
    }

    public boolean isFull(){
        return inscribedUsersSize() >= getUsersLimit();
    }

    public int inscribedUsersSize(){
        return inscribedUsers.size();
    }

    public boolean isUserInscribed(ChatUser user){
        return inscribedUsers.contains(user.getId());
    }

}
