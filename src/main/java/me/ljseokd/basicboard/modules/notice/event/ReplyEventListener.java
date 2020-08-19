package me.ljseokd.basicboard.modules.notice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.notice.Notice;
import me.ljseokd.basicboard.modules.notice.NoticeRepository;
import me.ljseokd.basicboard.modules.notification.Notification;
import me.ljseokd.basicboard.modules.notification.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class ReplyEventListener {

    private final NoticeRepository noticeRepository;
    private final NotificationRepository notificationRepository;
    @EventListener
    public void handleReplyCreatedEvent(ReplyCreatedEvent replyCreatedEvent){
        Account writer = replyCreatedEvent.getReply().getAccount();
        Long writerId = writer.getId();
        Notice notice = replyCreatedEvent.getReply().getNotice();
        String title = "게시글 '" + notice.getTitle() + "' 알림";
        String message = "게시글 '" + notice.getTitle() + "'에 "+writer.getNickname() + "님이 댓글을 남겼습니다.";

        List<Account> accounts = noticeRepository.findAccountWithNotificationById(notice.getId(), writerId);
        if (accounts.size() > 0){
            accounts = accounts.stream().filter(account -> account.getId() != writerId).collect(Collectors.toList());
            List<Notification> notifications = new ArrayList<>();

            accounts.forEach(account -> notifications.add(new Notification(title, message, account)));
            notificationRepository.saveAll(notifications);
        }

    }
}
