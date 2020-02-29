package com.ws.controller;

import com.ws.service.ApplyService;
import com.ws.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class TaskController {
    @Autowired
    private ApplyService applyService;
    @Autowired
    private RoomService roomService;

    @Scheduled(fixedRate = 10000)
    public void tasks(){
        //设置结束时间小于当前时间的待审批申请 状态为申请超时
        applyService.updateLessThanNowApplying();

        //设置结束时间小于当前时间的已通过审批申请 状态为结束使用
        applyService.updateLessThanNowApplyed();
    }


}
