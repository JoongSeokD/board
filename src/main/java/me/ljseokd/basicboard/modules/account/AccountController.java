package me.ljseokd.basicboard.modules.account;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.dto.ProfileDto;
import me.ljseokd.basicboard.modules.main.form.SignUpForm;
import me.ljseokd.basicboard.modules.main.validator.SignUpFormValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }



    @GetMapping(value = "/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname,
                              @CurrentAccount Account account,
                              Model model){
        Account viewAccount = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException(nickname));

        model.addAttribute("isOwner", viewAccount.isOwner(account));
        model.addAttribute("profileDto", modelMapper.map(viewAccount, ProfileDto.class));

        return "account/profile";
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
