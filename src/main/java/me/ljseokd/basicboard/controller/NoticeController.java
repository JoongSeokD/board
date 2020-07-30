package me.ljseokd.basicboard.controller;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.domain.Account;
import me.ljseokd.basicboard.domain.CurrentAccount;
import me.ljseokd.basicboard.domain.Notice;
import me.ljseokd.basicboard.form.NoticeForm;
import me.ljseokd.basicboard.repository.NoticeRepository;
import me.ljseokd.basicboard.service.NoticeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {


    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;

    @GetMapping("/new")
    public String createNoticeForm(Model model){
        model.addAttribute(new NoticeForm());
        return "notice/create-notice-form";
    }

    @PostMapping("/new")
    public String createNotice(@CurrentAccount Account account,
                               @Valid @ModelAttribute NoticeForm noticeForm,
                               Errors errors,
                               Model model){

        if (errors.hasErrors()){
            model.addAttribute("noticeForm",noticeForm);
            return "/notice/create-notice-form";
        }

        Long createdId = noticeService.createNotice(account, noticeForm);
        return "redirect:/notice/" + createdId + "/view";
    }

    @GetMapping("/{noticeId}/view")
    public String noticeView(@CurrentAccount Account account,
                             @PathVariable("noticeId") Long noticeId,
                             Model model){
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(noticeId)));
        model.addAttribute("isWriter", notice.isWriter(account));
        model.addAttribute(notice);

        return "notice/view";
    }
}
