package me.ljseokd.basicboard.modules.notice.event;

import lombok.Getter;
import me.ljseokd.basicboard.modules.notice.reply.Reply;

@Getter
public class ReplyCreatedEvent{

    private Reply reply;

    public ReplyCreatedEvent(Reply newReply) {
        this.reply = newReply;
    }
}
