package com.ws.controller.sys;

import com.ws.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LogController {
    @Autowired
    private LogService logService;

}
