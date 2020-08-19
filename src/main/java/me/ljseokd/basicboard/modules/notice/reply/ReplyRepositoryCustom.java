package me.ljseokd.basicboard.modules.notice.reply;

import me.ljseokd.basicboard.modules.notice.reply.dto.ReplyDto;

import java.util.List;


public interface ReplyRepositoryCustom {

    List<ReplyDto> findByReply(Long noticeId);
}
