package tech.xavi.wschatspringvue.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    private String id;
    @Column(nullable = false)
    private String roomTopic;
    @ManyToOne
    private ChatUser admin;
    @Column
    private String password;
    @Column(nullable = false)
    private int usersLimit;
    @Column(nullable = false)
    private int activeUsers;
    @Column(nullable = false)
    private boolean showInLobby;
    @Column(nullable = false)
    private LocalDateTime lastActivity;

}
