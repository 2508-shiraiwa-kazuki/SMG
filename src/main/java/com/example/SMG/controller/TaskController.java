package com.example.SMG.controller;

import com.example.SMG.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping
    public ModelAndView top(){
        ModelAndView mav = new ModelAndView();
        List<TaskForm> contentData = taskService.findTask();
        mav.setViewName("/");
        mav.addObject("tasks", contentData);
        return mav;
    }
}
