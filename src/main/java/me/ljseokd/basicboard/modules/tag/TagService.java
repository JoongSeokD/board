package me.ljseokd.basicboard.modules.tag;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.notice.Notice;
import me.ljseokd.basicboard.modules.notice.NoticeRepository;
import me.ljseokd.basicboard.modules.notice.NoticeTag;
import me.ljseokd.basicboard.modules.notice.NoticeTagRepository;
import me.ljseokd.basicboard.modules.notice.form.TagForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeTagRepository noticeTagRepository;

    public void createTag(Long noticeId, Account account, TagForm tagForm) {
        Notice notice = noticeRepository.findAccountFetchById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));

        String tagTitle = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(tagTitle);
        if (notice.getAccount().isOwner(account) && tag == null){
            Tag saveTag = tagRepository.save(new Tag(tagTitle));
            noticeTagRepository.save(new NoticeTag(notice, saveTag));
        }
    }
}
