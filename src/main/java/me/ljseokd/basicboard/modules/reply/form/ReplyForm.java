package me.ljseokd.basicboard.modules.reply.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReplyForm {

    @NotBlank
    String contents;
}