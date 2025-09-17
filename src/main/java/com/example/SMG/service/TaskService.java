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

        // 条件①「開始日と終了日の間」findByLimitDateBetween(start, end); if文不要
        // 条件②「～を含む」findByContentContaining(keyword); if文不要？
        // 条件③「ステータスの状態」findByStatus(status); 値が0の時は無視するようなif文が必要
        // Jpa文 findByLimitDateBetween And ContentContaining And Status

        List<Task> results;
        if(keyword != null && status != 0) {
//            results = taskRepository.findByLimitDateBetweenAndContentContainingAndStatus(start, end, keyword, status);
            results = taskRepository.findTop1000ByLimitDateBetweenAndContentAndStatusOrderByLimitDateAsc(start, end, keyword, status);
        } else if(keyword == null && status != 0){
//            results = taskRepository.findTop1000ByLimitDateBetweenAndContentOrderByLimitDateAsc(start, end, keyword);
            results = taskRepository.findTop1000ByLimitDateBetweenAndStatusOrderByLimitDateAsc(start, end, status);
        } else if(keyword != null){
            results = taskRepository.findTop1000ByLimitDateBetweenAndContentOrderByLimitDateAsc(start, end, keyword);
        } else {
            results = taskRepository.findTop1000ByLimitDateBetweenOrderByLimitDateAsc(start, end);
        }
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
            task.setLimitDate(String.valueOf(result.getLimitDate()));
            task.setCreatedDate(result.getCreatedDate());
            task.setUpdatedDate(result.getUpdatedDate());
            tasks.add(task);
        }
        return tasks;
    }

    /*
     * レコード追加・更新
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
    private Task setTaskEntity(TaskForm reqTask, Timestamp limitDate){
        Task task = new Task();
//        task.setId(reqTask.getId());
        task.setContent(reqTask.getContent());
        task.setStatus(reqTask.getStatus());
        task.setLimitDate(limitDate);

        if (Integer.valueOf(reqTask.getId()) != null) {
            task.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return task;
    }
}
