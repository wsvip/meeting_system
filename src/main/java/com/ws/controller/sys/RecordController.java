package com.ws.controller.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.annotation.SLog;
import com.ws.bean.*;
import com.ws.common.utils.ResultUtil;
import com.ws.common.utils.UuidUtils;
import com.ws.service.AttendService;
import com.ws.service.CommissionerService;
import com.ws.service.RecordService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/v1/api/sys/record")
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private CommissionerService commissionerService;
    @Autowired
    private AttendService attendService;

    @RequestMapping("/index")
    @RequiresPermissions("sys.record")
    public Object index() {
        return "sys/record/index";
    }

    @RequestMapping("/recordListData")
    @ResponseBody
    @SLog(operate = "查看会议记录列表")
    @RequiresPermissions("sys.apply")
    public Object recordListData(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam(value = "recordCondition", required = false) String recordCondition) {
        Page<Sys_record> iPage = new Page<>(page, limit);
        Sys_User user = (Sys_User) SecurityUtils.getSubject().getPrincipal();
        List<Sys_record> recordlist = recordService.recordListByPage(iPage, recordCondition,user.getId());
        int count = recordService.count();
        return ResultUtil.layuiPageData(0, null, count, recordlist);
    }

    @RequestMapping("/addRecord")
    @RequiresPermissions("sys.record.add")
    public Object addRecord() {
        return "sys/record/add";
    }


    @SLog(operate = "新增会议记录")
    @RequestMapping(value = "/addRecordDo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.record.add")
    public Object addRoom(String recordName, String roomName, String roomId, String host, String hostId, String commissionerId,String attendId,String startTime,String content) {
        String[] time = startTime.split(" - ");
        Sys_User user = (Sys_User) SecurityUtils.getSubject().getPrincipal();
        Sys_record record = new Sys_record();
        String uuid = UuidUtils.getUuid();
        record.setId(uuid);
        record.setRecordName(recordName);
        record.setRoomName(roomName);
        record.setRoomId(roomId);
        record.setHost(host);
        record.setHostId(hostId);
        record.setStartTime(time[0]);
        record.setEndTime(time[1]);
        record.setContent(content);
        record.setRecorder(user.getId());
        boolean save = recordService.save(record);
        String[] commissionerIds = commissionerId.split(",");
        String[] attendIds = attendId.split(",");
        ArrayList<Sys_record_commissioner> commissioners = new ArrayList<>();
        ArrayList<Sys_record_attend> attends = new ArrayList<>();

        for (String userId : commissionerIds) {
            Sys_record_commissioner commissioner = new Sys_record_commissioner();
            commissioner.setRecordId(uuid);
            commissioner.setUserId(userId);
            commissioners.add(commissioner);
        }
        for (String userId : attendIds) {
            Sys_record_attend attend = new Sys_record_attend();
            attend.setRecordId(uuid);
            attend.setUserId(userId);
            attends.add(attend);
        }
        boolean b = commissionerService.saveBatch(commissioners, commissioners.size());
        boolean b1 = attendService.saveBatch(attends, attends.size());
        if (save&&b&&b1){
            return ResultUtil.success(null, "新增会议记录成功！");
        }else{
            return ResultUtil.success(null, "新增会议记录失败！");

        }
    }

    @RequestMapping(value = "/editRecord")
    @RequiresPermissions("sys.record.edit")
    public Object editRoom(@RequestParam("id") String id, HttpServletRequest request){
        Sys_record recordData = recordService.getById(id);
        recordData.setStartTime(recordData.getStartTime()+" - "+recordData.getEndTime());
        HashMap<String, String> attendIdsAndNames = recordService.attendNamesListByRecordId(id);
        HashMap<String, String> commIdsAndNames = recordService.commissionerNamesListByRecordId(id);
        request.setAttribute("recordData",recordData);
        request.setAttribute("attendNames",attendIdsAndNames);
        request.setAttribute("commissionerNames",commIdsAndNames);
        return "sys/record/edit";
    }

    @SLog(operate = "修改会议记录")
    @RequestMapping(value = "/editRecordDo",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.record.edit")
    public Object editRoomDo(Sys_record record,String commissionerId,String attendId){
        String startTime = record.getStartTime();
        String[] time = startTime.split(" - ");
        record.setStartTime(time[0]);
        record.setEndTime(time[1]);
        boolean flag = recordService.updateById(record);
        ArrayList<Sys_record_commissioner> commissioners = new ArrayList<>();
        ArrayList<Sys_record_attend> attends = new ArrayList<>();
        String[] commissionerIds = commissionerId.split(",");
        String[] attendIds = attendId.split(",");
        String recordId = record.getId();
        for (String userId : commissionerIds) {
            Sys_record_commissioner commissioner = new Sys_record_commissioner();
            commissioner.setRecordId(recordId);
            commissioner.setUserId(userId);
            commissioners.add(commissioner);
        }
        for (String userId : attendIds) {
            Sys_record_attend attend = new Sys_record_attend();
            attend.setRecordId(recordId);
            attend.setUserId(userId);
            attends.add(attend);
        }
        attendService.removeByMap(new HashMap<String,Object>(){{put("recordId",recordId);}});
        attendService.saveBatch(attends,attends.size());
        commissionerService.removeByMap(new HashMap<String,Object>(){{put("recordId",recordId);}});
        commissionerService.saveBatch(commissioners,commissioners.size());
        if (flag) {
            return ResultUtil.success(0, "修改成功！");
        }else{
            return ResultUtil.error(1,"修改失败!");
        }
    }

    @SLog(operate = "删除会议记录")
    @RequestMapping(value = "/delRecordData",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.record.del")
    public Object delRecordData(String recordId){
        boolean flag = recordService.removeById(recordId);
        //删除两个中间表中该记录相关的参会人员、参会专员
        attendService.removeByMap(new HashMap<String,Object>(){{put("recordId",recordId);}});
        commissionerService.removeByMap(new HashMap<String,Object>(){{put("recordId",recordId);}});
        if (flag){
            return ResultUtil.success(flag,"删除成功");
        }else {
            return ResultUtil.error(1,"删除失败");
        }

    }

    @RequestMapping(value = "/detail")
    public Object detail(@RequestParam("id") String id, HttpServletRequest request){
        Sys_record recordData = recordService.getById(id);
        recordData.setStartTime(recordData.getStartTime()+" - "+recordData.getEndTime());
        HashMap<String, String> attendIdsAndNames = recordService.attendNamesListByRecordId(id);
        HashMap<String, String> commIdsAndNames = recordService.commissionerNamesListByRecordId(id);
        request.setAttribute("recordData",recordData);
        request.setAttribute("attendNames",attendIdsAndNames);
        request.setAttribute("commissionerNames",commIdsAndNames);
        return "sys/record/detail";
    }
}
