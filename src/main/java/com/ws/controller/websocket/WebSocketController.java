package com.ws.controller.websocket;

import com.ws.common.websocket.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author WS-
 */
@Controller
@RequestMapping("/websocket")
public class WebSocketController {;
    @GetMapping("/socket/{socketId}")
    public ModelAndView socket(@PathVariable String socketId){
        ModelAndView modelAndView = new ModelAndView("/socket");
        modelAndView.addObject("socketId",socketId);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/push/{socketId}")
    public void pushToWeb(@PathVariable String socketId,String message){
        try {
            WebSocketServer.sendInfo(message,socketId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
