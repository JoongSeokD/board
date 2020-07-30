package me.ljseokd.basicboard.form;

import lombok.Data;
import me.ljseokd.basicboard.domain.Account;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
public class NoticeForm {

    @NotBlank
    @Length(max = 50)
    private String title;

    @Lob
    @NotBlank
    private String contents;

}
