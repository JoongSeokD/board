package me.ljseokd.basicboard.modules.notice;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Data
public class NoticeForm {

    private Long id;

    @NotBlank
    @Length(max = 50)
    private String title;

    @Lob
    @NotBlank
    private String contents;

}
