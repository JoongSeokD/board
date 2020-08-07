package me.ljseokd.basicboard.modules.tag;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.modules.notice.NoticeTag;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Tag {


    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @OneToMany(mappedBy = "tag")
    private Set<NoticeTag> noticeTags = new HashSet<>();

    public Tag(String title) {
        this.title = title;
    }
}
