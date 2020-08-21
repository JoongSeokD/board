package me.ljseokd.basicboard.modules.event;

import me.ljseokd.basicboard.modules.event.dto.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepositoryCustom {
    Page<EventDto> page(Pageable pageable);
}
