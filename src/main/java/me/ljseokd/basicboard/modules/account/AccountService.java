package me.ljseokd.basicboard.modules.account;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.form.ProfileForm;
import me.ljseokd.basicboard.modules.main.form.SignUpForm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(SignUpForm signUpForm) {
        Account account = new Account(signUpForm.getUsername(),
                passwordEncoder.encode(signUpForm.getPassword()));
        Account save = accountRepository.save(account);
        login(account);
        return save.getId();
    }



    private void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("USER_ROLE")));

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findByNickname(username);

        return new UserAccount(account);
    }

    private Account findByNickname(String username) {
        return accountRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public void changeProfile(Account account, ProfileForm profileForm) {
        Account byNickname = findByNickname(account.getNickname());
        byNickname.changeProfile(profileForm);
    }
}
