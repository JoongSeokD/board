package me.ljseokd.basicboard.modules.notice.reply;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import me.ljseokd.basicboard.modules.notice.event.ReplyCreatedEvent;
import me.ljseokd.basicboard.modules.notice.Notice;
import me.ljseokd.basicboard.modules.notice.NoticeRepository;
import me.ljseokd.basicboard.modules.notice.reply.form.ReplyForm;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final NoticeRepository noticeRepository;
    private final AccountRepository accountRepository;
    private final ReplyRepository replyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void addReply(Account account, Long noticeId, ReplyForm replyForm) {
        String nickname = account.getNickname();
        Notice notice = noticeRepository.findByIdFetchReply(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));
        Account findAccount = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException(nickname));
        Reply newReply = new Reply(findAccount, notice, replyForm.getContents());
        // TODO 댓글을 작성하면 댓글 작성자 이외의 사람에게 알림이 가야함
        eventPublisher.publishEvent(new ReplyCreatedEvent(newReply));
    }

    public void modifyContents(Long replyId, String contents) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(replyId)));
        reply.modifyContents(contents);
    }
}
