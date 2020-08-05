package me.ljseokd.basicboard.modules.account;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.dto.ProfileDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccountSettingController {

    private final AccountRepository accountRepository;

    private final ModelMapper modelMapper;

    @GetMapping("/settings")
    public String settingsMainPage(@CurrentAccount Account account, Model model){
        Account findByNickname = accountRepository.findByNickname(account.getNickname())
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(account.getId())));

        model.addAttribute("profileDto", modelMapper.map(findByNickname, ProfileDto.class));

        return "account/settings";

    }

}
