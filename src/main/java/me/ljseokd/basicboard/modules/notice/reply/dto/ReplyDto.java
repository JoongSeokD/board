package me.ljseokd.basicboard.modules.notice.reply.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyDto {

    private Long id;
    private String writer;
    private LocalDateTime createdDate;
    private String contents;
    private boolean isOwner;

    @QueryProjection
    public ReplyDto(Long id, String writer, LocalDateTime createdDate, String contents) {
        this.id = id;
        this.writer = writer;
        this.createdDate = createdDate;
        this.contents = contents;
    }
}
