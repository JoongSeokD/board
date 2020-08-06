package me.ljseokd.basicboard.modules.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }


}
