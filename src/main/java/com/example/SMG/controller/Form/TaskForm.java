package com.example.SMG.controller.Form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskForm {

    private Integer id;

    @NotNull(message = "タスクを入力してください")
    @Pattern(regexp = "(?s)^(?![\\s　]*$).+", message = "タスクを入力してください")
    @Size(min = 0,  max = 140, message = "タスクは140文字以内で入力してください")
    private String content;

    private int status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "期限を設定してください")
    private LocalDateTime limitDate;

    private Timestamp createdDate;

    private Timestamp updatedDate;
}
