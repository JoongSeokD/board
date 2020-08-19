package me.ljseokd.basicboard.modules.notice;

import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.notice.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeRepositoryCustom {

    Page<NoticeDto> page(Pageable pageable);

    List<Account> findAccountWithNotificationById(Long id, Long writerId);
}
