package me.ljseokd.basicboard.modules.reply;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.ljseokd.basicboard.modules.reply.dto.QReplyDto;
import me.ljseokd.basicboard.modules.reply.dto.ReplyDto;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static me.ljseokd.basicboard.modules.account.QAccount.account;
import static me.ljseokd.basicboard.modules.notice.QNotice.notice;
import static me.ljseokd.basicboard.modules.reply.QReply.reply;

@Transactional(readOnly = true)
public class ReplyRepositoryImpl implements ReplyRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ReplyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ReplyDto> findByReply(Long noticeId) {
        List<ReplyDto> results = queryFactory.select(new QReplyDto(
                reply.id,
                reply.account.nickname.as("writer"),
                reply.createDateTime.as("createdDate"),
                reply.contents))
                .from(reply)
                .leftJoin(reply.notice, notice)
                .leftJoin(notice.account, account)
                .where(notice.id.eq(noticeId))
                .orderBy(reply.id.desc()).fetch();
        return results;
    }
}
