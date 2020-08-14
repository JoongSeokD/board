package me.ljseokd.basicboard.modules.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
    @Query("select n from Notice n join fetch n.account where n.id = ?1")
    Optional<Notice> findAccountFetchById(Long noticeId);

    @Query("select n from Notice n join fetch n.replyList where n.id = ?1")
    Optional<Notice> findByIdFetchReply(Long noticeId);

    @Query("select n from Notice n left join fetch n.fileList left join fetch n.account where n.id = ?1")
    Optional<Notice> findNoticeView(Long noticeId);

    @Query("select n from Notice n left join fetch n.replyList left join fetch n.fileList where n.id = ?1")
    Optional<Notice> findByIdFetchFileAndReply(Long noticeId);
}
