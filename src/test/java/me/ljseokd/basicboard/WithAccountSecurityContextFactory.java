package me.ljseokd.basicboard;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.form.SignUpForm;
import me.ljseokd.basicboard.service.AccountService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;


@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        String username = withAccount.value();

        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername(username);
        signUpForm.setPassword("12345678");

        accountService.join(signUpForm);

        UserDetails principal = accountService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        return context;
    }
}
