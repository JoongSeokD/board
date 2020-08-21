package me.ljseokd.basicboard.modules.event;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.ljseokd.basicboard.modules.event.dto.EventDto;
import me.ljseokd.basicboard.modules.event.dto.QEventDto;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static me.ljseokd.basicboard.modules.account.QAccount.account;
import static me.ljseokd.basicboard.modules.event.QEvent.event;

@Transactional
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public EventRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<EventDto> page(Pageable pageable) {
        QueryResults<EventDto> results = queryFactory
                .select(new QEventDto(
                        event.createdBy.nickname,
                        event.title,
                        event.thumbnail,
                        event.recruitmentStartDate,
                        event.recruitmentEndDate,
                        event.eventsStartDate,
                        event.eventsEndDate,
                        event.limitOfEnrollments,
                        event.free,
                        event.price,
                        event.eventType.stringValue(),
                        event.id))
                .from(event)
                .leftJoin(event.createdBy, account)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(event.id.desc())
                .fetchResults();

        List<EventDto> eventDtos = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(eventDtos, pageable, total);
    }
}
