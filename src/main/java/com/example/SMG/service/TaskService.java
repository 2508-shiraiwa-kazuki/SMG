package com.example.SMG.service;

import com.example.SMG.controller.Form.TaskForm;
import com.example.SMG.repository.TaskRepository;
import com.example.SMG.repository.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    /*
     * 新規タスク追加
     */
    public  void saveTask(TaskForm reqTask){
        Task saveTask = setTaskEntity(reqTask);
        taskRepository.save(saveTask);
    }


    /*
     * リクエストから取得した情報をentityに設定
     */
    private Task setTaskEntity(TaskForm reqTask){
        Task task = new Task();
        task.setContent(reqTask.getContent());
        task.setLimitDate(reqTask.getLimitDate());

        return task;
    }
}
