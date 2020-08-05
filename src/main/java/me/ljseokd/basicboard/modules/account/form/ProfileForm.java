package me.ljseokd.basicboard.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProfileForm {

    @Length(max = 50)
    private String bio;
}
