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

    public void newEvent(Account account, EventForm eventForm) {
        eventRepository.save(new Event(account, eventForm));
    }
}
