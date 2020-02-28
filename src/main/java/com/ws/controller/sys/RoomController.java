package com.ws.controller.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.annotation.SLog;
import com.ws.bean.Sys_User;
import com.ws.bean.Sys_apply;
import com.ws.bean.Sys_room;
import com.ws.common.utils.DateUtil;
import com.ws.common.utils.ResultUtil;
import com.ws.common.utils.Strings;
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
@RequestMapping("/v1/api/sys/room")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ApplyService applyService;

    @RequestMapping("/index")
    @RequiresPermissions("sys.room")
    public Object index() {
        return "sys/room/index";
    }

    @RequestMapping("/roomListData")
    @ResponseBody
    @SLog(operate = "查看会议室列表")
    @RequiresPermissions("sys.room")
    public Object roomListData(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam(value = "roomCondition", required = false) String roomCondition) {
        Page<Sys_room> iPage = new Page<>(page, limit);
        List<Sys_room> roomlist = roomService.roomListByPage(iPage, roomCondition);
        int count = roomService.count();
        return ResultUtil.layuiPageData(0, null, count, roomlist);
    }

    @RequestMapping("/addRoom")
    @RequiresPermissions("sys.room.add")
    public Object addRoom() {
        return "sys/room/add";
    }


    @SLog(operate = "新增会议室")
    @RequestMapping(value = "/addRoomDo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.room.add")
    public Object addRoom(String roomName, String roomNo, String capacity, String principal, String principalId, String remark) {
        Sys_room room = new Sys_room();
        room.setRoomName(roomName);
        room.setRoomNo(roomNo);
        if (!Strings.isBlank(capacity)) {
            room.setCapacity(Integer.parseInt(capacity));
        }
        room.setPrincipal(principal);
        room.setPrincipalId(principalId);
        room.setCreateTime(DateUtil.getDateTime());
        room.setStatus(2);
        room.setRemark(remark);
        roomService.save(room);
        return ResultUtil.success(null, "新增会议室成功！");
    }

    @RequestMapping(value = "/editRoom")
    @RequiresPermissions("sys.room.edit")
    public Object editRoom(@RequestParam("id") String id, HttpServletRequest request){
        Sys_room roomData = roomService.getById(id);
        request.setAttribute("roomData",roomData);
        return "sys/room/edit";
    }

    @SLog(operate = "修改会议室")
    @RequestMapping(value = "/editRoomDo",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.room.edit")
    public Object editRoomDo(Sys_room room){
        boolean flag = roomService.updateById(room);
        Sys_apply apply = new Sys_apply();

        apply.setRoomName(room.getRoomName());
        apply.setRoomNo(room.getRoomNo());
        /*更新申请表中会议室信息*/
        boolean b=applyService.updateRoom(room.getId(),room.getRoomName(),room.getRoomNo());
        if (flag) {
            return ResultUtil.success(0, "修改成功！");
        }else{
            return ResultUtil.error(1,"修改失败!");
        }
    }

    @RequestMapping(value = "/applyRoom")
    @RequiresPermissions("sys.room.apply")
    public Object applyRoom(@RequestParam("id") String id, HttpServletRequest request){
        Sys_room roomData = roomService.getById(id);
        request.setAttribute("roomData",roomData);
        return "sys/room/apply";
    }

    @SLog(operate = "申请会议室")
    @RequestMapping(value = "/applyRoomDo",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.room.apply")
    public Object applyRoomDo(Sys_apply application){
        Sys_User user= (Sys_User) SecurityUtils.getSubject().getPrincipal();
        String startTime = application.getStartTime();
        String[] time = startTime.split(" - ");
        application.setStartTime(time[0]);
        application.setEndTime(time[1]);
        application.setApplicant(user.getNickname());
        application.setApplicantId(user.getId());
        application.setStatus(0);
        application.setAppTime(DateUtil.getDateTime());
        String roomId = application.getRoomId();
        boolean flag = applyService.save(application);
        if (flag) {
            Sys_room room = new Sys_room();
            room.setId(roomId);
            room.setStatus(0);
            roomService.updateById(room);
            return ResultUtil.success(flag, "申请成功！");
        }else{
            return ResultUtil.error(1,"申请失败!");
        }
    }

    @SLog(operate = "删除会议室")
    @RequestMapping(value = "/delRoomData",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.room.del")
    public Object delRoomData(String roomId){
        boolean flag = roomService.removeById(roomId);
        //更新申请状态
        boolean b=applyService.updateApplyStatus(roomId,5);
        if (flag){
            return ResultUtil.success(flag,"删除成功");
        }else {
            return ResultUtil.error(1,"删除失败");
        }

    }
}
