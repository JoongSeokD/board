package me.ljseokd.basicboard.modules.notice;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Long createNotice(Account account, NoticeForm noticeForm) {
        Notice notice = new Notice(noticeForm.getTitle(), noticeForm.getContents());
        notice.addAccount(account);
        noticeRepository.save(notice);
        return notice.getId();
    }

    public boolean isWriter(Account noticeAccount, Account account) {
        if (account != null){
            return noticeAccount.equals(account);
        }
        return false;
    }

    public void update(Long noticeId, NoticeForm noticeForm) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));

        notice.update(noticeForm);

    }
}
