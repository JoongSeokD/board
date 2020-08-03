package me.ljseokd.basicboard.config;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.domain.Account;
import me.ljseokd.basicboard.domain.Notice;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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
            }
        }
    }
}
