package me.ljseokd.basicboard.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.auditing.DateTimeBaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Notice extends DateTimeBaseEntity {

    @Id @GeneratedValue
    @Column(name = "notice_id")
    private Long id;

    private String title;
    @Lob
    private String contents;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "account_id")
    Account account;

    public void addAccount(Account account){
        this.account = account;
        account.getNotices().add(this);
    }

    public Notice(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public boolean isWriter(Account account) {
        return this.account.equals(account);
    }
}