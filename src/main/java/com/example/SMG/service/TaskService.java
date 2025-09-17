package com.example.SMG.service;

import com.example.SMG.controller.Form.TaskForm;
import com.example.SMG.repository.TaskRepository;
import com.example.SMG.repository.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.sql.Timestamp;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    /*
     * 新規タスク追加
     */
    public  void saveTask(TaskForm reqTask, Timestamp limitDate){
        Task saveTask = setTaskEntity(reqTask, limitDate);
        taskRepository.save(saveTask);
    }

    /*
     * タスク削除
     */
    public void  deleteTask(Integer id){
        taskRepository.deleteById(id);
    }

    /*
     * リクエストから取得した情報をentityに設定
     */
    private Task setTaskEntity(TaskForm reqTask, Timestamp limitDate){
        Task task = new Task();
        task.setContent(reqTask.getContent());
        task.setStatus(reqTask.getStatus());
        task.setLimitDate(limitDate);
        return task;
    }
}
