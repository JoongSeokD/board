package me.ljseokd.basicboard.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Notice {

    @Id @GeneratedValue
    @Column(name = "notice_id")
    private Long id;

    private String title;
    @Lob
    private String contents;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime lastModifiedTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    Account account;

    public void addAccount(Account account){
        this.account = account;
        account.getNotices().add(this);
    }

}
