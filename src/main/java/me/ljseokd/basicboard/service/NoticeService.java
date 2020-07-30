package me.ljseokd.basicboard.service;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.domain.Account;
import me.ljseokd.basicboard.domain.Notice;
import me.ljseokd.basicboard.form.NoticeForm;
import me.ljseokd.basicboard.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
