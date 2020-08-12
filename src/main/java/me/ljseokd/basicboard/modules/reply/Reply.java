package me.ljseokd.basicboard.modules.reply;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.infra.auditing.DateTimeBaseEntity;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.notice.Notice;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Reply extends DateTimeBaseEntity {

    @Id @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @Lob
    private String contents;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public Reply(Account account, Notice notice, String contents) {
        this.account = account;
        this.notice = notice;
        this.contents = contents;
        this.notice.addReply(this);
    }

    public void modifyContents(String contents) {
        this.contents = contents;
    }
}
