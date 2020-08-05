package me.ljseokd.basicboard.modules.notice.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDto {

    private String writer;
    private String title;
    private LocalDateTime lastModifiedTime;
    private Long noticeId;

    @QueryProjection
    public NoticeDto(String writer, String title, LocalDateTime lastModifiedTime, Long noticeId) {
        this.writer = writer;
        this.title = title;
        this.lastModifiedTime = lastModifiedTime;
        this.noticeId = noticeId;
    }
}
