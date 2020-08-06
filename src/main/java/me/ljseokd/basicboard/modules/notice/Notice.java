package me.ljseokd.basicboard.modules.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.infra.auditing.DateTimeBaseEntity;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.notice.form.NoticeForm;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
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

    @ManyToOne(fetch = LAZY)
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

    public Notice(String title, String contents, Account account) {
        this.title = title;
        this.contents = contents;
        this.account = account;
    }

    public void update(NoticeForm noticeForm) {
        title = noticeForm.getTitle();
        contents = noticeForm.getContents();
    }
}