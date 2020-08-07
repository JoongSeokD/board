package me.ljseokd.basicboard.modules.notice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface NoticeTagRepository extends JpaRepository<NoticeTag, Long> {
    Set<NoticeTag> findByNoticeId(Long id);
}
