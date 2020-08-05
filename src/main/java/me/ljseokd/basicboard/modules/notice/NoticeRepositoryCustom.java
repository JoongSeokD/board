package me.ljseokd.basicboard.modules.notice;

import me.ljseokd.basicboard.modules.notice.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<NoticeDto> page(Pageable pageable);

}
