package com.ws.controller.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.annotation.SLog;
import com.ws.bean.Sys_User;
import com.ws.bean.Sys_apply;
import com.ws.bean.Sys_room;
import com.ws.common.utils.DateUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/v1/api/sys/apply")
public class ApplyController {
    @Autowired
    private ApplyService applyService;
    @Autowired
    private RoomService roomService;

    @RequestMapping("/index")
    @RequiresPermissions("sys.apply")
    public Object index() {
        return "sys/apply/index";
    }

    @RequestMapping("/applyListData")
    @ResponseBody
    @SLog(operate = "查看申请列表")
    @RequiresPermissions("sys.apply")
    public Object applyListData(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam(value = "applyCondition", required = false) String applyCondition) {
        Sys_User user = (Sys_User) SecurityUtils.getSubject().getPrincipal();
        Page<Sys_apply> iPage = new Page<>(page, limit);
        List<Sys_apply> applylist = applyService.applyListByPage(iPage, applyCondition,user.getId());
        int count = applyService.count();
        return ResultUtil.layuiPageData(0, null, count, applylist);
    }

    @RequestMapping("/applyedListData")
    @ResponseBody
    @SLog(operate = "查看已通过申请列表")
    @RequiresPermissions("sys.apply")
    public Object applyedListData(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Sys_User user = (Sys_User) SecurityUtils.getSubject().getPrincipal();
        Page<Sys_apply> iPage = new Page<>(page, limit);
        List<Sys_apply> applyedlist = applyService.applyedListByPage(iPage,user.getId());
        int count = applyService.count();
        return ResultUtil.layuiPageData(0, null, count, applyedlist);
    }

    @RequestMapping(value = "/editApply")
    public Object editApply(@RequestParam("id") String id, HttpServletRequest request) {
        Sys_apply applyData = applyService.getById(id);
        String startTime = applyData.getStartTime();
        String endTime = applyData.getEndTime();
        startTime = startTime + " - " + endTime;
        applyData.setStartTime(startTime);
        request.setAttribute("applyData", applyData);
        return "sys/apply/edit";
    }

    @SLog(operate = "重新申请")
    @RequestMapping(value = "/editApplyDo", method = RequestMethod.POST)
    @ResponseBody
    public Object editApplyDo(Sys_apply apply) {

        String startTime = apply.getStartTime();
        String[] time = startTime.split(" - ");
        apply.setStartTime(time[0]);
        apply.setEndTime(time[1]);
        apply.setAppTime(DateUtil.getDateTime());
        apply.setStatus(0);
        boolean flag = applyService.updateById(apply);
        Sys_room room = new Sys_room();
        room.setId(apply.getRoomId());
        room.setStatus(0);
        boolean _flag = roomService.updateById(room);
        if (flag&&_flag) {
            return ResultUtil.success(flag, "重新申请成功！");
        } else {
            return ResultUtil.error(1, "重新申请失败!");
        }
    }


    @SLog(operate = "取消申请")
    @RequestMapping(value = "/cancelApply", method = RequestMethod.POST)
    @ResponseBody
    public Object cancelApply(String applyId, String roomId) {
        Sys_apply apply = new Sys_apply();
        apply.setId(applyId);
        apply.setStatus(4);
        boolean flag = applyService.updateById(apply);
        Sys_room room = new Sys_room();
        room.setId(roomId);
        room.setStatus(2);
        boolean _flag = roomService.updateById(room);
        if (flag&&_flag) {
            return ResultUtil.success(flag, "取消成功！");
        } else {
            return ResultUtil.error(1, "取消失败!");
        }
    }

    @SLog(operate = "删除申请")
    @RequestMapping(value = "/delApply", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.apply.del")
    public Object delApply(String applyId, String roomId) {
        boolean flag = applyService.removeById(applyId);
        Sys_room room = new Sys_room();
        room.setId(roomId);
        room.setStatus(2);
        boolean _flag = roomService.updateById(room);
        if (flag&&_flag) {
            return ResultUtil.success(flag, "删除成功");
        } else {
            return ResultUtil.error(1, "删除失败");
        }

    }

    @SLog(operate = "结束使用会议室")
    @RequestMapping(value = "/used", method = RequestMethod.POST)
    @ResponseBody
    public Object used(String applyId, String roomId) {
        Sys_apply apply = new Sys_apply();
        //将状态置为6，表示结束使用该会议室
        apply.setId(applyId);
        apply.setStatus(6);
        boolean _flag = applyService.updateById(apply);
        Sys_room room = new Sys_room();
        room.setId(roomId);
        room.setStatus(2);
        boolean flag = roomService.updateById(room);
        if (flag&&_flag) {
            return ResultUtil.success(flag, "已结束使用");
        } else {
            return ResultUtil.error(1, "操作失败");
        }

    }


}
