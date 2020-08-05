package me.ljseokd.basicboard.modules.account;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.form.ProfileForm;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class AccountSettingController {

    private final AccountRepository accountRepository;

    private final ModelMapper modelMapper;

    private static final String ACCOUNT = "account";
    private static final String SETTINGS = "/settings";
    private static final String DESCRIPTION = "/description";

    @GetMapping(DESCRIPTION)
    public String descriptionForm(@CurrentAccount Account account, Model model){
        Account findByNickname = getFindByNickname(account.getNickname());

        model.addAttribute("profileForm", modelMapper.map(findByNickname, ProfileForm.class));

        return ACCOUNT + SETTINGS + DESCRIPTION;
    }

    private Account getFindByNickname(String nickname) {
        return accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException(nickname));
    }

    @PostMapping(DESCRIPTION)
    public String descriptionSubmit(@CurrentAccount Account account,
                                    @Valid @ModelAttribute ProfileForm profileForm,
                                    Errors errors, Model model){

        if (errors.hasErrors()){
            model.addAttribute(profileForm);
            return ACCOUNT + SETTINGS + DESCRIPTION;
        }

        Account findAccount = getFindByNickname(account.getNickname());
        findAccount.changeDescription(profileForm);

        return "redirect:" + SETTINGS + DESCRIPTION;
    }




}
