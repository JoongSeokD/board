package me.ljseokd.basicboard.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-z0-9]{2,10}$")
    @Length(min = 2, max = 10)
    private String username;

    @NotBlank
    @Length(min = 8, max = 50)
    private String password;
}
