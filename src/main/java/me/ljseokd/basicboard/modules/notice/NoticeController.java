package me.ljseokd.basicboard.modules.notice;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.CurrentAccount;
import me.ljseokd.basicboard.modules.notice.form.NoticeForm;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {


    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/new")
    public String createNoticeForm(Model model){
        model.addAttribute(new NoticeForm());
        return "notice/create-notice-form";
    }

    @PostMapping("/new")
    public String createNotice(@CurrentAccount Account account,
                               @Valid @ModelAttribute NoticeForm noticeForm,
                               Errors errors,Model model){

        if (errors.hasErrors()){
            model.addAttribute("noticeForm",noticeForm);
            return "/notice/create-notice-form";
        }

        Long createdId = noticeService.createNotice(account, noticeForm);
        return "redirect:/notice/" + createdId + "/view";
    }

    @GetMapping("/{noticeId}/view")
    public String noticeView(@CurrentAccount Account account,
                             @PathVariable Long noticeId,
                             Model model){

        Notice notice = noticeRepository.findAccountFetchById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));

        model.addAttribute("isOwner", notice.getAccount().isOwner(account));
        model.addAttribute(notice);

        return "notice/view";
    }

    @GetMapping("/{noticeId}/update")
    public String noticeUpdateForm(@PathVariable Long noticeId,
                                   Model model){
        Notice notice = findById(noticeId);
        NoticeForm noticeForm = modelMapper.map(notice, NoticeForm.class);
        model.addAttribute(noticeForm);

        return "notice/update";
    }

    private Notice findById(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));
    }

    @PostMapping("/{noticeId}/update")
    public String noticeUpdateSubmit(@Valid @ModelAttribute NoticeForm noticeForm,
                                     Errors errors,Model model,
                                     @PathVariable Long noticeId){
        if (errors.hasErrors()){
            model.addAttribute(noticeForm);
            return "notice/update";
        }
        noticeService.update(noticeId, noticeForm);
        return "redirect:/notice/" + noticeId + "/view";
    }

    @PostMapping("/{noticeId}/delete")
    public String noticeDelete(@PathVariable Long noticeId,
                               RedirectAttributes attributes){
        Notice notice = findById(noticeId);
        noticeRepository.delete(notice);
        attributes.addFlashAttribute("message",
                notice.getTitle() + " 게시글이 삭제 되었습니다.");
        return "redirect:/notice/list";
    }

    @GetMapping("/list")
    public String list(Pageable pageable, Model model){
        model.addAttribute("noticePage", noticeRepository.page(pageable));
        return "notice/list";
    }
}
