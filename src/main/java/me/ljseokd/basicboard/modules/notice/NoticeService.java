package me.ljseokd.basicboard.modules.notice;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import me.ljseokd.basicboard.modules.file.AttacheFileRepository;
import me.ljseokd.basicboard.modules.notice.form.NoticeForm;
import me.ljseokd.basicboard.modules.notice.reply.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AccountRepository accountRepository;
    private final ReplyRepository replyRepository;
    private final AttacheFileRepository attacheFileRepository;


    public Long createNotice(Account account, NoticeForm noticeForm) {

        String nickname = account.getNickname();
        Account saveAccount = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException(nickname));

        Notice notice = new Notice(noticeForm.getTitle(), noticeForm.getContents());
        notice.addAccount(saveAccount);
        noticeRepository.save(notice);

        return notice.getId();
    }


    public Long update(Long noticeId, NoticeForm noticeForm) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));

        notice.update(noticeForm);
        return notice.getId();
    }

    public String delete(Long noticeId) {
        Notice notice = noticeRepository.findByIdFetchFileAndReply(noticeId)
                .orElseThrow(()-> new IllegalArgumentException(String.valueOf(noticeId)));

        if (notice.getReplyList().size() > 0){
            replyRepository.deleteInBatch(notice.getReplyList());
            notice.getReplyList().clear();
        }
        if (notice.getFileList().size() > 0){
            attacheFileRepository.deleteInBatch(notice.getFileList());
            notice.getFileList().clear();
        }

        noticeRepository.delete(notice);
        return notice.getTitle();
    }

    public Notice viewCountIncrease(Long noticeId) {
        Notice notice = noticeRepository.findNoticeView(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));;
        notice.countIncrease();
        return notice;
    }
}
