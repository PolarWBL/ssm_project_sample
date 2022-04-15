package com.ctgu.ssm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Boliang Weng
 */
@Controller
@RequestMapping("test")
public class TestController {
    @GetMapping("t1")
    public ModelAndView test1(){
        //测试ftl页面跳转
        return new ModelAndView("/test");
    }

    @GetMapping("t2")
    @ResponseBody
    public Map<String, Object> test2(){
        Map<String, Object> map = new HashMap<>();
        map.put("test", "测试文本");
        return map;
    }
}
