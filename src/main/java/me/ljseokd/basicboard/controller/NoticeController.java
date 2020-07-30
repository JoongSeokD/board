package me.ljseokd.basicboard.controller;

import me.ljseokd.basicboard.form.NoticeForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

    @GetMapping("/notice/new")
    public String createNoticeForm(Model model){
        model.addAttribute(new NoticeForm());
        return "notice/create-notice-form";
    }
}
