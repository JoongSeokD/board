package me.ljseokd.basicboard.modules.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AttacheFileRepository extends JpaRepository<AttacheFile, Long> {
}
