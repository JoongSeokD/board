package me.ljseokd.basicboard.modules.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.modules.notice.Notice;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachedFile {

    @Id @GeneratedValue
    @Column(name = "atch_file_id")
    private Long id;

    private String orgFileName;
    private String ext;
    private String saveFileName;
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

}
