package com.example.SMG.controller.Form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class TaskForm {

    @NotNull(message = "不正なパラメータです")
    @Pattern(regexp = "^[0-9]+$", message = "不正なパラメータです")
    private int id;

    @NotBlank(message = "タスクを入力してください")
    @Size(min = 1, max =  140, message = "タスクは140文字以内で入力してください")
    private String content;

    private int status;

    @NotBlank(message = "期限を設定してください")
    private String limitDate;

    private Timestamp createdDate;

    private Timestamp updatedDate;

}
