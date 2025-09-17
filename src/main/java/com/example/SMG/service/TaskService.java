package com.example.SMG.service;

import com.example.SMG.controller.Form.TaskForm;
import com.example.SMG.repository.TaskRepository;
import com.example.SMG.repository.entity.Task;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    /*
     * タスク取得＋絞り込み
     */
    public List<TaskForm> findTask(String startDate, String endDate, int status, String keyword){
        // 開始日の設定
        String startTime;
        if(!StringUtils.isBlank(startDate)){
            startTime = startDate + " 00:00:00";
        } else {
            startTime = "2020-01-01 00:00:00";
        }
        Timestamp start = Timestamp.valueOf(startTime);
        // 終了日の設定
        String endTime;
        if(!StringUtils.isBlank(endDate)){
            endTime = endDate + " 23:59:59";
        } else {
            endTime = "2100-12-31 23:59:59";
        }
        Timestamp end = Timestamp.valueOf(endTime);
        // 条件①「開始日と終了日の間」findByLimitDateBetween(start, end);
        // 条件②「ステータスの状態」findByStatus(status);
        // 条件③「～を含む」findByContentContaining(keyword);
        List<Task> results = taskRepository.findByLimitDateBetweenAndStatusAndContentContaining(start, end, status, keyword);
        return setTaskForm(results);
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<TaskForm> setTaskForm(List<Task> results){
        List<TaskForm> tasks = new ArrayList<>();
        for(int i = 0; i < results.size(); i++){
            TaskForm task = new TaskForm();
            Task result = results.get(i);
            task.setId(result.getId());
            task.setContent(result.getContent());
            task.setStatus(result.getStatus());
            task.setLimitDate(result.getLimitDate());
            task.setCreatedDate(result.getCreatedDate());
            task.setUpdatedDate(result.getUpdatedDate());
            tasks.add(task);
        }
        return tasks;
    }

    /*
     * レコード追加・更新
     */
    public  void saveTask(TaskForm reqTask){
        Task saveTask = setTaskEntity(reqTask);
        taskRepository.save(saveTask);
    }

    /*
     * 編集対象レコード取得処理
     */
    public TaskForm editTask(Integer id) {
        List<Task> results = new ArrayList<>();
        results.add(taskRepository.findById(id).orElse(null));
        List<TaskForm> tasks = setTaskForm(results);
        return tasks.get(0);
    }

    /*
     * リクエストから取得した情報をentityに設定
     */
    private Task setTaskEntity(TaskForm reqTask){
        Task task = new Task();
        task.setId(reqTask.getId());
        task.setContent(reqTask.getContent());
        task.setStatus(reqTask.getStatus());
        task.setLimitDate(reqTask.getLimitDate());

        if (Integer.valueOf(reqTask.getId()) != null) {
            task.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return task;
    }
}
