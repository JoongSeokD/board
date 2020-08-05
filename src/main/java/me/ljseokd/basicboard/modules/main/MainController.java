package me.ljseokd.basicboard.modules.main;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.AccountService;
import me.ljseokd.basicboard.modules.main.form.SignUpForm;
import me.ljseokd.basicboard.modules.main.validator.SignUpFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute(new SignUpForm());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors){

        if (errors.hasErrors()){
            return "sign-up";
        }
        accountService.join(signUpForm);
        return "redirect:/";
    }
}
