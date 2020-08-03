package me.ljseokd.basicboard.repository;

import me.ljseokd.basicboard.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<NoticeDto> page(Pageable pageable);

}
