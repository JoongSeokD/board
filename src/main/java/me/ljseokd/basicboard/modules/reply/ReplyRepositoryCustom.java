package me.ljseokd.basicboard.modules.reply;

import me.ljseokd.basicboard.modules.reply.dto.ReplyDto;

import java.util.List;


public interface ReplyRepositoryCustom {

    List<ReplyDto> findByReply(Long noticeId);
}
