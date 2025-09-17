package com.example.SMG.controller;

import com.example.SMG.controller.Form.TaskForm;
import com.example.SMG.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView top(@RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate,
                            @RequestParam(defaultValue = "1") int status,
                            @RequestParam(required = false) String keyword) {
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

    @PostMapping("/add")
    public ModelAndView addTask(@ModelAttribute("formModel") @Validated TaskForm taskForm,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", taskForm);
            return new ModelAndView("redirect:/new");
        }

        taskService.saveTask(taskForm);

        return new ModelAndView("redirect:/");
    }

    /*
     * タスク編集画面表示処理
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editTask(@PathVariable @Validated Integer id,
                                 BindingResult result, RedirectAttributes redirectAttributes) {
        // 取得したタスクIDをチェック
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("formModel", result);
            return new ModelAndView("redirect:/");
        }

        // タスク取得処理
        TaskForm task = taskService.editTask(id);
        // タスクの存在チェック
        if (task == null) {
            String errorMessage = "不正なパラメータです";
            redirectAttributes.addFlashAttribute("formModel", errorMessage);
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
                                   @Validated @ModelAttribute("formModel") TaskForm task,
                                   BindingResult result) {

        // タスク内容をチェック
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/edit");
            return mav;
        }

        // タスク更新処理
        task.setId(id);
        taskService.saveTask(task);

        // TOP画面表示処理
        return new ModelAndView("redirect:/");
    }
}
