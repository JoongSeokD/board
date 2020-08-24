package me.ljseokd.basicboard.modules.event;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.event.form.EventForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    public Long newEvent(Account account, EventForm eventForm) {
        Event newEvent = eventRepository.save(new Event(account, eventForm));
        return newEvent.getId();
    }

    public void updateEvent(Long eventId, EventForm eventForm) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(eventId)));
        event.update(eventForm);
    }
}
