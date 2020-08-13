package me.ljseokd.basicboard.modules.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.infra.auditing.DateTimeBaseEntity;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.file.AttacheFile;
import me.ljseokd.basicboard.modules.notice.form.NoticeForm;
import me.ljseokd.basicboard.modules.reply.Reply;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Account account;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "notice")
    private Set<NoticeTag> noticeTags = new HashSet<>();

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private Set<AttacheFile> fileList = new HashSet<>();

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

    public void addReply(Reply reply) {
        this.replyList.add(reply);
    }

    public void addFiles(Set<AttacheFile> attacheFiles) {
        fileList = attacheFiles;
        fileList.forEach(attacheFile -> attacheFile.addNotice(this));
    }
}
