package com.example.SMG.controller;

import com.example.SMG.controller.Form.TaskForm;
import com.example.SMG.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

public class TaskController {
    @Autowired
    TaskService taskService;

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
