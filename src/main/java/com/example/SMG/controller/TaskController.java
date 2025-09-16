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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
