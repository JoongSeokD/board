package me.ljseokd.basicboard.modules.account;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.dto.ProfileDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

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

}
