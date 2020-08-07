package me.ljseokd.basicboard.modules.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.modules.tag.Tag;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class NoticeTag {

    @Id @GeneratedValue
    @Column(name = "notice_tag_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public NoticeTag(Notice notice, Tag tag){
        this.notice = notice;
        this.tag = tag;
        notice.getNoticeTags().add(this);
        tag.getNoticeTags().add(this);
    }
}
