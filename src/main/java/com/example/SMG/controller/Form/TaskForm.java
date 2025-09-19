package com.example.SMG.controller.Form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskForm {

    private Integer id;

    @NotBlank(message = "タスクを入力してください")
<<<<<<< HEAD
    @Size(min = 1,  max = 140, message = "タスクは140文字以内で入力してください")
=======
    @Pattern(regexp = "^(?![　]+$).*", message = "タスクを入力してください")
    @Size(min = 0,  max = 140, message = "タスクは140文字以内で入力してください")
>>>>>>> 8354c96e0ded6f40708db9c3e4ded4e3c32165c3
    private String content;

    private int status;

    @NotNull(message = "期限を設定してください")
    private LocalDateTime limitDate;

    private Timestamp createdDate;

    private Timestamp updatedDate;
}
