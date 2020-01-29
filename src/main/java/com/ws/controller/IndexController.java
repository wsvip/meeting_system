package com.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/api")
public class IndexController {

    @RequestMapping("/index")
    public Object index(){

        return "sys/user/index";
    }
}
