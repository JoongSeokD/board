package me.ljseokd.basicboard.modules.event.dto;

import lombok.Data;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.event.EventType;

import java.time.LocalDateTime;

@Data
public class EventViewDto {

    private Long id;

    private String title;

    private String contents;
    private Account createdBy;

    private LocalDateTime recruitmentStartDate;
    private LocalDateTime recruitmentEndDate;
    private LocalDateTime eventsStartDate;
    private LocalDateTime eventsEndDate;

    private long limitOfEnrollments;

    private String thumbnail;

    private boolean free;

    private int price;

    private EventType eventType;
}
