package me.ljseokd.basicboard.modules.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
    @Query("select n from Notice n join fetch n.account where n.id = ?1")
    Optional<Notice> findAccountFetchById(Long noticeId);
}
