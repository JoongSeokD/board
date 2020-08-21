package me.ljseokd.basicboard.modules.event.validator;

import me.ljseokd.basicboard.modules.event.form.EventForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class EventFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return EventForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventForm  eventForm = (EventForm) target;

        if (isNotValidRecruitmentEndDate(eventForm)){
            errors.rejectValue("recruitmentEndDate", "wrong.datetime", "접수 종료 일을 정확히 입력하세요.");
        } if (isNotValidEventStartDate(eventForm)){
            errors.rejectValue("recruitmentEndDate", "wrong.datetime", "행사 시작 일을 정확히 입력하세요.");
        } if (isNotValidEventEndDate(eventForm)){
            errors.rejectValue("recruitmentEndDate", "wrong.datetime", "행사 종료 일을 정확히 입력하세요.");
        }
    }


    private boolean isNotValidRecruitmentEndDate(EventForm eventForm) {
        return eventForm.getRecruitmentEndDate().isBefore(LocalDateTime.now())
                || eventForm.getRecruitmentEndDate().isBefore(eventForm.getRecruitmentStartDate());
    }
    private boolean isNotValidEventStartDate(EventForm eventForm) {
        return eventForm.getEventsStartDate().isBefore(eventForm.getRecruitmentEndDate());
    }
    private boolean isNotValidEventEndDate(EventForm eventForm) {
        return eventForm.getEventsEndDate().isBefore(eventForm.getRecruitmentEndDate())
                || eventForm.getEventsEndDate().isBefore(eventForm.getEventsStartDate());
    }
}
