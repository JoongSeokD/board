package me.ljseokd.basicboard.modules.event.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {

    private Long id;

    private String createdBy;
    private String title;
    private String thumbnail;
    private LocalDateTime recruitmentStartDate;
    private LocalDateTime recruitmentEndDate;
    private LocalDateTime eventsStartDate;
    private LocalDateTime eventsEndDate;
    private long limitOfEnrollments;
    private boolean free;
    private int price;
    private String eventType;

    @QueryProjection
    public EventDto(String createdBy, String title, String thumbnail, LocalDateTime recruitmentStartDate,
                    LocalDateTime recruitmentEndDate, LocalDateTime eventsStartDate, LocalDateTime eventsEndDate,
                    long limitOfEnrollments, boolean free, int price, String eventType, Long id) {
        this.createdBy = createdBy;
        this.title = title;
        this.thumbnail = thumbnail;
        this.recruitmentStartDate = recruitmentStartDate;
        this.recruitmentEndDate = recruitmentEndDate;
        this.eventsStartDate = eventsStartDate;
        this.eventsEndDate = eventsEndDate;
        this.limitOfEnrollments = limitOfEnrollments;
        this.free = free;
        this.price = price;
        this.eventType = eventType;
        this.id = id;
    }
}
