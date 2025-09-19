package com.example.SMG.controller;

import com.example.SMG.controller.Form.TaskForm;
import com.example.SMG.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                            @RequestParam(required = false) String keyword) {
        ModelAndView mav = new ModelAndView();
        // タスク取得＋絞り込み
        List<TaskForm> taskData = taskService.findTask(startDate, endDate, status, keyword);



        // 現在日の取得
        Timestamp today = new Timestamp(System.currentTimeMillis());

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
    public ModelAndView newTask(Model model) {
        ModelAndView mav = new ModelAndView();

        if (!model.containsAttribute("formModel")) {
            TaskForm taskForm = new TaskForm();
            mav.addObject("formModel", taskForm);
        }

        //画面遷移先指定
        mav.addObject("/new");
        return mav;
    }

    /*
     * ステータス変更処理
     */
/*    @PutMapping("/change/{id}")
    public ModelAndView changeStatus(@PathVariable Integer id,
                                     @RequestParam String content,
                                     @RequestParam Integer status,
                                     @RequestParam String date) {
        // ステータス変更対象のタスク情報を設定
        TaskForm task = new TaskForm();
        task.setId(id);
        task.setContent(content);
        task.setStatus(status);
        // ステータス更新処理
        Timestamp limitDate = Timestamp.valueOf(date);
        taskService.saveTask(task, limitDate);
        // TOP画面表示処理
        return new ModelAndView("redirect:/");
    }
*/
    @PutMapping("/change/{id}")
    public ModelAndView changeStatus(@PathVariable Integer id,
                                     @RequestParam String content,
                                     @RequestParam Integer status,
                                     @RequestParam String limitDate) {

        TaskForm task = new TaskForm();
        task.setId(id);
        task.setContent(content);
        task.setStatus(status);

        Timestamp limitDateTs = Timestamp.valueOf(limitDate);
        taskService.saveTask(task, limitDateTs);

        return new ModelAndView("redirect:/");
    }


    /*
     *　新規タスク追加処理
     */
    @PostMapping("/add")
    public ModelAndView addTask(@ModelAttribute("formModel")@Validated TaskForm taskForm,
                                BindingResult result,
                                RedirectAttributes redirectAttributes){

        Timestamp limitDate = null;
        if(!StringUtils.isEmpty(taskForm.getLimitDate())){
            Timestamp today = new Timestamp(System.currentTimeMillis());
            LocalDateTime ldc = LocalDateTime.parse(taskForm.getLimitDate());
            limitDate = Timestamp.valueOf(ldc);

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
    public ModelAndView editTask(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        List<String> errorMessages = new ArrayList<>();

        // 取得したタスクIDをチェック
        if (id == null ||  id.toString().matches( "^\\\\d+$")) {
            errorMessages.add("不正なパラメータです");
            redirectAttributes.addFlashAttribute("formModel", errorMessages);
            return new ModelAndView("redirect:/");
        }

        // タスク取得処理
        TaskForm task = taskService.editTask(id);

        // タスクの存在チェック
        if (task == null) {
            errorMessages.add("不正なパラメータです");
            redirectAttributes.addFlashAttribute("formModel", errorMessages);
            return new ModelAndView("redirect:/");
        }

        // タスク編集画面表示処理
        ModelAndView mav = new ModelAndView();
        mav.addObject("formModel", task);
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * タスク編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateTask(@PathVariable Integer id,
                                   @ModelAttribute("formModel") @Validated TaskForm task,
                                   BindingResult result, RedirectAttributes redirectAttributes) {
        // タスク内容をチェック
        Timestamp limitDate = null;
        if (!StringUtils.isEmpty(task.getLimitDate())) {
            Timestamp today = new Timestamp(System.currentTimeMillis());
            limitDate = Timestamp.valueOf(LocalDateTime.parse(task.getLimitDate()));

            //今日の日付と入力された日付を比較し、過去の日付であればエラーを追加
            if (limitDate.before(today)) {
                FieldError fieldError = new FieldError(result.getObjectName(), "limitDate", "無効な日付です");
                result.addError(fieldError);
            }
        }
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
    public ModelAndView deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return new ModelAndView("redirect:/");
    }
}
