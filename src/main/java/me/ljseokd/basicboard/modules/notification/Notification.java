package me.ljseokd.basicboard.modules.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "notification_id")
    private Long id;

    private String title;
    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDateTime;

    private boolean checked;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @PrePersist
    private void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDateTime = now;
    }

    public Notification(String title, String message, Account account) {
        this.title = title;
        this.message = message;
        this.account = account;
        this.account.addNotification(this);
    }
}
