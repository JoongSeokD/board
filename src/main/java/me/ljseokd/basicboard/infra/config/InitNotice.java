package me.ljseokd.basicboard.infra.config;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.notice.Notice;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
