package me.ljseokd.basicboard.modules.notice;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import me.ljseokd.basicboard.modules.notice.form.NoticeForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AccountRepository accountRepository;


    public Long createNotice(Account account, NoticeForm noticeForm) {

        String nickname = account.getNickname();
        Account saveAccount = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException(nickname));

        Notice notice = new Notice(noticeForm.getTitle(), noticeForm.getContents());
        notice.addAccount(saveAccount);
        noticeRepository.save(notice);

        return notice.getId();
    }


    public void update(Long noticeId, NoticeForm noticeForm) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));

        notice.update(noticeForm);
    }
}
