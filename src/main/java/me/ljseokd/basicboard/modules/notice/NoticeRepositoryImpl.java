package me.ljseokd.basicboard.modules.notice;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.notice.dto.NoticeDto;
import me.ljseokd.basicboard.modules.notice.dto.QNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static me.ljseokd.basicboard.modules.account.QAccount.account;
import static me.ljseokd.basicboard.modules.notice.QNotice.notice;
import static me.ljseokd.basicboard.modules.notice.reply.QReply.reply;


public class NoticeRepositoryImpl implements NoticeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public NoticeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NoticeDto> page(Pageable pageable) {
        QueryResults<NoticeDto> results = queryFactory
                .select(new QNoticeDto(
                        notice.account.nickname,
                        notice.title,
                        notice.createDateTime,
                        notice.id))
                .from(notice)
                .leftJoin(notice.account, account)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.id.desc())
                .fetchResults();
        List<NoticeDto> noticeDtos = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(noticeDtos, pageable, total);
    }

    @Override
    public List<Account> findAccountWithNotificationById(Long noticeId, Long writerId) {
        List<Account> accounts = queryFactory
                .select(account)
                .distinct()
                .from(notice, reply, account)
                .join(reply.notice, notice)
                .where(notice.id.eq(noticeId))
                .fetch();
        return accounts;
    }
}
