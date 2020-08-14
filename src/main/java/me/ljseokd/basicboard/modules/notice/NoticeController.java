package me.ljseokd.basicboard.modules.notice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.CurrentAccount;
import me.ljseokd.basicboard.modules.file.AttacheFileRepository;
import me.ljseokd.basicboard.modules.file.AttacheFileService;
import me.ljseokd.basicboard.modules.file.dto.AttacheFileDto;
import me.ljseokd.basicboard.modules.notice.form.NoticeForm;
import me.ljseokd.basicboard.modules.notice.form.TagForm;
import me.ljseokd.basicboard.modules.reply.ReplyRepository;
import me.ljseokd.basicboard.modules.reply.ReplyService;
import me.ljseokd.basicboard.modules.reply.dto.ReplyDto;
import me.ljseokd.basicboard.modules.reply.form.ReplyForm;
import me.ljseokd.basicboard.modules.tag.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static com.google.common.net.HttpHeaders.*;
import static java.net.URLEncoder.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {


    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;
    private final TagService tagService;
    private final ReplyService replyService;
    private final ReplyRepository replyRepository;
    private final ObjectMapper objectMapper;
    private final AttacheFileService attacheFileService;
    private final AttacheFileRepository attacheFileRepository;

    @GetMapping("/new")
    public String createNoticeForm(Model model){
        model.addAttribute(new NoticeForm());
        return "notice/create-notice-form";
    }

    @PostMapping("/new")
    public String createNotice(@CurrentAccount Account account,
                               @Valid @ModelAttribute NoticeForm noticeForm,
                               Errors errors,Model model,
                               @RequestParam MultipartFile[] file){

        if (errors.hasErrors()){
            model.addAttribute("noticeForm", noticeForm);
            return "/notice/create-notice-form";
        }

        Long createdId = noticeService.createNotice(account, noticeForm);

        if (file.length > 0){
            attacheFileService.addAttacheFile(createdId, file);
        }

        return "redirect:/notice/" + createdId + "/view";
    }

    @GetMapping("/{noticeId}/view")
    public String noticeView(@CurrentAccount Account account,
                             @PathVariable Long noticeId,
                             Model model){

        Notice notice = noticeService.viewCountIncrease(noticeId);

        model.addAttribute("isOwner", notice.getAccount().isOwner(account));
        model.addAttribute(notice);
        model.addAttribute(new ReplyForm());

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
        String title = noticeService.delete(noticeId);
        attributes.addFlashAttribute("message",
                title + " 게시글이 삭제 되었습니다.");
        return "redirect:/notice/list";
    }

    @GetMapping("/{fileId}")
    public ResponseEntity attacheFileDownLoad(@PathVariable Long fileId)
            throws IOException {

        AttacheFileDto fileDto = attacheFileService.getResourceFile(fileId);

        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment;filename=" + encode(fileDto.getFileName(), "UTF-8"))
                .contentType(fileDto.getMediaType())
                .contentLength(fileDto.getFile().length())
                .body(fileDto.getResource());
    }

    @GetMapping("/list")
    public String list(Pageable pageable, Model model){
        model.addAttribute("noticePage", noticeRepository.page(pageable));
        return "notice/list";
    }

    @PostMapping("/{noticeId}/tag/add")
    @ResponseBody
    public ResponseEntity tagAdd(@CurrentAccount Account account,
                         @PathVariable Long noticeId,
                         @RequestBody TagForm tagForm){

        tagService.createTag(noticeId, account, tagForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{noticeId}/reply/add")
    @ResponseBody
    public ResponseEntity replyAdd(@CurrentAccount Account account,
                                   @PathVariable Long noticeId,
                                   @Valid @RequestBody ReplyForm replyForm, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body("내용이 없는 글 작성");
        }

        replyService.addReply(account, noticeId, replyForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{noticeId}/reply")
    @ResponseBody
    public ResponseEntity replyList (@CurrentAccount Account account,
                                     @PathVariable Long noticeId)
            throws JsonProcessingException {

        List<ReplyDto> byReply = replyRepository.findByReply(noticeId);
        if (account != null){
            byReply.forEach(r ->
                    r.setOwner(r.getWriter().equals(account.getNickname())));
        }

        return ResponseEntity.ok(objectMapper.writeValueAsString(byReply));
    }

    @PostMapping("/{replyId}/reply/modify")
    @ResponseBody
    public ResponseEntity replyModify(@PathVariable Long replyId,
                                      @Valid @RequestBody ReplyForm replyForm,
                                      Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body("내용이 없는 글 작성");
        }
        replyService.modifyContents(replyId, replyForm.getContents());
        return ResponseEntity.ok().build();
    }



}
