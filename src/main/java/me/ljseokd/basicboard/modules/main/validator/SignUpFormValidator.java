package me.ljseokd.basicboard.modules.main.validator;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.main.form.SignUpForm;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;
        String username = signUpForm.getUsername();
        Account account = accountRepository.findByNickname(username).orElse(null);
        if (account != null){
            errors.rejectValue("username", "invalid.username", new Object[]{username}, "이미 존재하는 이름입니다.");
        }
    }
}
