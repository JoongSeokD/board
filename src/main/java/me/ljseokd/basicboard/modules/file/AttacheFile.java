package me.ljseokd.basicboard.modules.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.modules.notice.Notice;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttacheFile {

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

    public AttacheFile(String orgFileName, String ext, String saveFileName, Long fileSize) {
        this.orgFileName = orgFileName;
        this.ext = ext;
        this.saveFileName = saveFileName;
        this.fileSize = fileSize;
    }

    public void addNotice(Notice notice) {
        this.notice = notice;
    }
}
