package com.zerobase.foodlier.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestCnt {
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
