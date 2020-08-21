package me.ljseokd.basicboard.modules.event.form;

import lombok.Data;
import me.ljseokd.basicboard.modules.event.EventType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class EventForm {

    @NotBlank
    private String title;

    @NotBlank
    private String contents;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime recruitmentStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime recruitmentEndDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime eventsStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime eventsEndDate;

    @Min(2)
    private long limitOfEnrollments = 2;

    private String thumbnail;

    private boolean free = true;

    private int price;

    private EventType eventType = EventType.FCFS;
}
