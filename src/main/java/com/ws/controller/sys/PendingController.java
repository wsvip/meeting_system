package com.ws.controller.sys;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.annotation.SLog;
import com.ws.bean.Sys_User;
import com.ws.bean.Sys_apply;
import com.ws.bean.Sys_room;
import com.ws.common.utils.ResultUtil;
import com.ws.service.ApplyService;
import com.ws.service.RoomService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/v1/api/sys/pending")
public class PendingController {
    @Autowired
    private ApplyService applyService;
    @Autowired
    private RoomService roomService;

    @RequestMapping("/index")
    @RequiresPermissions("sys.pend")
    public Object index() {
        return "sys/pending/index";
    }

    @RequestMapping("/pendingListData")
    @ResponseBody
    @SLog(operate = "查看待办事项列表")
    @RequiresPermissions("sys.pend")
    public Object applyListData(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam(value = "pendingCondition", required = false) String pendingCondition) {
        Sys_User user= (Sys_User) SecurityUtils.getSubject().getPrincipal();
        Page<Sys_apply> iPage = new Page<>(page, limit);
        List<Sys_apply> applylist = applyService.pendingListByPage(iPage, pendingCondition,user.getId());
        int count = applyService.count();
        return ResultUtil.layuiPageData(0, null, count, applylist);
    }

    @SLog(operate = "审批申请单")
    @RequestMapping(value = "/approvalApply", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.pend.approval")
    public Object approvalApply(String applyId, String roomId,boolean flag) {
        Sys_room room = new Sys_room();
        room.setId(roomId);
        Sys_apply apply = new Sys_apply();
        apply.setId(applyId);
        //审批通过
        if (flag){
            apply.setStatus(1);
            room.setStatus(1);
        }else{
            apply.setStatus(2);
            room.setStatus(2);
        }
        boolean _flag = applyService.updateById(apply);

        roomService.updateById(room);
        if (_flag) {
            return ResultUtil.success(_flag, "取消成功！");
        } else {
            return ResultUtil.error(1, "取消失败!");
        }
    }

}
