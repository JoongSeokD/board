package me.ljseokd.basicboard.infra.config;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.event.Event;
import me.ljseokd.basicboard.modules.event.EventType;
import me.ljseokd.basicboard.modules.event.form.EventForm;
import me.ljseokd.basicboard.modules.notice.Notice;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class InitNotice {

    private final InitNoticeService initNoticeService;

    @PostConstruct
    public void init(){
        initNoticeService.init();
    }


    @Component
    static class InitNoticeService{
        @PersistenceContext private EntityManager em;

        @Transactional
        public void init(){
            Account test = new Account("test", "12341234");
            em.persist(test);
            for (int i = 0; i < 100; i++){
                em.persist(new Notice("title" + i, "contents" + i, test));
                EventForm eventForm = new EventForm();
                eventForm.setTitle("title" + i);
                eventForm.setContents("contents" + i);
                eventForm.setFree(true);
                eventForm.setRecruitmentStartDate(LocalDateTime.now().plusDays(1));
                eventForm.setRecruitmentEndDate(LocalDateTime.now().plusDays(2));
                eventForm.setEventsStartDate(LocalDateTime.now().plusDays(3));
                eventForm.setEventsEndDate(LocalDateTime.now().plusDays(4));
                em.persist(new Event(test, eventForm));

            }
        }
    }
}
