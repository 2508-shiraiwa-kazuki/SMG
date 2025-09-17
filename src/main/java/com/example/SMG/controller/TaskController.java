package com.example.SMG.controller;

import com.example.SMG.controller.Form.TaskForm;
import com.example.SMG.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class TaskController {
    @Autowired
    TaskService taskService;

    /*
     * TOP画面表示
     */
    @GetMapping
    public ModelAndView top(@RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate,
                            @RequestParam(defaultValue = "0") int status,
                            @RequestParam(required = false) String keyword
                            ){
        ModelAndView mav = new ModelAndView();

        // タスク取得＋絞り込み
        List<TaskForm> taskData = taskService.findTask(startDate, endDate, status, keyword);
        // 現在日の取得
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String today = sdf.format(now);

        mav.setViewName("/top");
        mav.addObject("tasks", taskData);
        mav.addObject("today", today);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("status", status);
        mav.addObject("keyword", keyword);
        return mav;
    }

    /*
     * 新規タスク追加画面表示
     */
    @GetMapping("/new")
    public ModelAndView newTask(Model model){
        ModelAndView mav = new ModelAndView();

        if(!model.containsAttribute("formModel")){
            TaskForm taskForm = new TaskForm();
            mav.addObject("formModel", taskForm);
        }

        //画面遷移先指定
        mav.addObject("/new");
        return mav;
    }

    /*
     *　新規タスク追加処理
     */
    @PostMapping("/add")
    public ModelAndView addTask(@ModelAttribute("formModel")@Validated TaskForm taskForm, BindingResult result, RedirectAttributes redirectAttributes){
        Timestamp limitDate = null;
        if(!StringUtils.isEmpty(taskForm.getLimitDate())){
            Timestamp today = new Timestamp(System.currentTimeMillis());
            limitDate = Timestamp.valueOf(taskForm.getLimitDate() + " 23:59:59");

            //今日の日付と入力された日付を比較し、過去の日付であればエラーを追加
            if(limitDate.before(today)){
                FieldError fieldError = new FieldError(result.getObjectName(), "limitDate", "無効な日付です");
                result.addError(fieldError);
            }
        }

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", taskForm);
            return new ModelAndView("redirect:/new");
        }

        taskForm.setStatus(1);

        taskService.saveTask(taskForm, limitDate);

        return new ModelAndView("redirect:/");
    }

    /*
     * タスク編集画面表示処理
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editTask(@PathVariable Integer id, BindingResult result) {
        // 取得したタスクIDをチェック
        if (result.hasErrors()) {
            return new ModelAndView("redirect:/");
        }

        // タスク取得処理
        ModelAndView mav = new ModelAndView();
        TaskForm task = taskService.editTask(id);

        // タスクIDの存在チェック
        if (task == null) {
            String errorMessages = "不正なパラメータです";
            return new ModelAndView("redirect:/");
        }

        // タスク編集画面表示処理
        mav.addObject("formModel", task);
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * タスク編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateTask(@PathVariable Integer id,
                                   @Validated @ModelAttribute("formModel") TaskForm task,
                                   BindingResult result) {

        Timestamp limitDate = null;
        if(!StringUtils.isEmpty(task.getLimitDate())){
            Timestamp today = new Timestamp(System.currentTimeMillis());
            limitDate = Timestamp.valueOf(task.getLimitDate() + " 23:59:59");

            //今日の日付と入力された日付を比較し、過去の日付であればエラーを追加
            if(limitDate.before(today)){
                FieldError fieldError = new FieldError(result.getObjectName(), "limitDate", "無効な日付です");
                result.addError(fieldError);
            }
        }

        // タスク内容をチェック
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/edit");
            return mav;
        }

        // タスク更新処理
        task.setId(id);
        taskService.saveTask(task, limitDate);

        // TOP画面表示処理
        return new ModelAndView("redirect:/");
    }

    /*
     * タスク削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteTask(@PathVariable Integer id){
        taskService.deleteTask(id);
        return new ModelAndView("redirect:/");
    }
}
