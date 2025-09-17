package com.example.SMG.controller;

import com.example.SMG.controller.Form.TaskForm;
import com.example.SMG.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView top(@RequestParam(required = false) Date startDate,
                            @RequestParam(required = false) Date endDate,
                            @RequestParam(defaultValue = "1") int status,
                            @RequestParam(required = false) String keyword
                            ){
        ModelAndView mav = new ModelAndView();
        // タスク取得＋絞り込み
        List<TaskForm> taskData = taskService.findTask(startDate, endDate, status, keyword);
        // 現在日の取得
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today = sdf.format(now);

        mav.setViewName("/");
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

    @PostMapping("/add")
    public ModelAndView addTask(@ModelAttribute("formModel")@Validated TaskForm taskForm, BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", taskForm);
            return new ModelAndView("redirect:/new");
        }

        taskService.saveTask(taskForm);

        return new ModelAndView("redirect:/");
    }
}
