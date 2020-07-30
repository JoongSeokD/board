package me.ljseokd.basicboard.repository;

import me.ljseokd.basicboard.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
