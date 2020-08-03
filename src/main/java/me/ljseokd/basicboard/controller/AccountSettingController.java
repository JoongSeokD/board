package me.ljseokd.basicboard.controller;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.domain.Account;
import me.ljseokd.basicboard.domain.CurrentAccount;
import me.ljseokd.basicboard.dto.ProfileDto;
import me.ljseokd.basicboard.repository.AccountRepository;
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
