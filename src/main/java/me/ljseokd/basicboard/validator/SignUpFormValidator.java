package me.ljseokd.basicboard.validator;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.domain.Account;
import me.ljseokd.basicboard.form.SignUpForm;
import me.ljseokd.basicboard.repository.AccountRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        Account account = accountRepository.findByName(username).orElse(null);
        if (account != null){
            errors.rejectValue("username", "invalid.username", new Object[]{username}, "이미 존재하는 이름입니다.");
        }
    }
}
