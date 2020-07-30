package me.ljseokd.basicboard.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Data
public class NoticeForm {

    @NotBlank
    @Length(max = 50)
    private String title;

    @Lob
    @NotBlank
    private String contents;

}
