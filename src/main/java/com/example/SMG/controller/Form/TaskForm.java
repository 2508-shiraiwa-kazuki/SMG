package com.example.SMG.controller.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class TaskForm {

    private int id;

    @NotBlank(message = "タスクを入力してください")
    private String content;

    @NotBlank(message = "期限を設定してください")
    private Timestamp limitDate;

    private Timestamp createdDate;

    private Timestamp updatedDate;

}
