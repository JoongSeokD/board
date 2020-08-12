package me.ljseokd.basicboard.modules.reply;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import me.ljseokd.basicboard.modules.notice.Notice;
import me.ljseokd.basicboard.modules.notice.NoticeRepository;
import me.ljseokd.basicboard.modules.reply.form.ReplyForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final NoticeRepository noticeRepository;
    private final AccountRepository accountRepository;
    private final ReplyRepository replyRepository;


    public void addReply(Account account, Long noticeId, ReplyForm replyForm) {
        String nickname = account.getNickname();
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));
        Account findAccount = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException(nickname));

        new Reply(findAccount, notice, replyForm.getContents());
    }

    public void modifyContents(Long replyId, String contents) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(replyId)));
        reply.modifyContents(contents);
    }
}
