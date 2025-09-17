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

import java.sql.Timestamp;

@Controller
public class TaskController {
    @Autowired
    TaskService taskService;

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
     * タスク削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteTask(@PathVariable Integer id){
        taskService.deleteTask(id);
        return new ModelAndView("redirect:/");
    }
}
