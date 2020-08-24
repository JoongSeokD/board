package me.ljseokd.basicboard.modules.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.event.form.EventForm;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Event {

    @Id @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    private String title;

    @ManyToOne(fetch = LAZY)
    private Account createdBy;

    @Lob
    private String contents;

    private LocalDateTime recruitmentStartDate;
    private LocalDateTime recruitmentEndDate;
    private LocalDateTime eventsStartDate;
    private LocalDateTime eventsEndDate;

    private long limitOfEnrollments;

    @Lob
    private String thumbnail;

    private boolean free;

    private int price;

    @Enumerated(STRING)
    private EventType eventType;

    public Event(Account account, EventForm eventForm) {
        this.createdBy = account;
        this.title = eventForm.getTitle();
        this.contents = eventForm.getContents();
        this.limitOfEnrollments = eventForm.getLimitOfEnrollments();
        this.free = eventForm.isFree();
        if (!eventForm.isFree()){
            price = eventForm.getPrice();
        }
        this.eventType = eventForm.getEventType();
        this.thumbnail = eventForm.getThumbnail();
        this.recruitmentStartDate = eventForm.getRecruitmentStartDate();
        this.recruitmentEndDate = eventForm.getRecruitmentEndDate();
        this.eventsStartDate = eventForm.getEventsStartDate();
        this.eventsEndDate = eventForm.getEventsEndDate();
    }

    public void update(EventForm eventForm) {
        title = eventForm.getTitle();
        contents = eventForm.getContents();
        limitOfEnrollments = eventForm.getLimitOfEnrollments();
        thumbnail = eventForm.getThumbnail();
        recruitmentStartDate = eventForm.getRecruitmentStartDate();
        recruitmentEndDate = eventForm.getRecruitmentEndDate();
        eventsStartDate = eventForm.getEventsStartDate();
        eventsEndDate = eventForm.getEventsEndDate();
        free = eventForm.isFree();
        price = eventForm.getPrice();
        eventType = eventForm.getEventType();
    }
}
