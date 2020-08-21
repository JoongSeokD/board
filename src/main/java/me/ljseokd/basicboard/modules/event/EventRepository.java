package me.ljseokd.basicboard.modules.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    @Query("select e from Event e  join fetch e.createdBy where e.id = ?1")
    Optional<Event> findByAccountWithId(Long eventId);
}
