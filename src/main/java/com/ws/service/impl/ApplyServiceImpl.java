package com.ws.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bean.Sys_apply;
import com.ws.mapper.ApplyMapper;
import com.ws.mapper.RoomMapper;
import com.ws.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Sys_apply> implements ApplyService {
    @Autowired
    private ApplyMapper applyMapper;
    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<Sys_apply> applyListByPage(Page<Sys_apply> iPage, String applyCondition, String userId) {
        List<Sys_apply> applyList = applyMapper.applyListByPage(iPage, applyCondition, userId);
        return applyList;
    }

    @Override
    public boolean updateRoom(String id, String roomName, String roomNo) {
        boolean flag = applyMapper.updateRoom(id, roomName, roomNo);
        return flag;
    }

    @Override
    public boolean updateApplyStatus(String roomId, int status) {
        boolean flag = applyMapper.updateApplyStatus(roomId, status);
        return flag;
    }

    @Override
    public List<Sys_apply> pendingListByPage(Page<Sys_apply> iPage, String applyCondition, String userId) {
        List<Sys_apply> pendingList = applyMapper.pendingListByPage(iPage, applyCondition, userId);
        return pendingList;
    }

    @Override
    public List<Sys_apply> applyedListByPage(Page<Sys_apply> iPage, String userId) {
        List<Sys_apply> applyedList = applyMapper.applyedListByPage(iPage, userId);
        return applyedList;
    }

    @Override
    public void updateLessThanNowApplying() {
        //获取状态为待审批且结束使用时间小于当前时间的申请
        List<Sys_apply> applys = applyMapper.getLessThanNowApplying();
        if (applys.size() > 0) {
            ArrayList<String> applyIds = new ArrayList<>();
            ArrayList<String> roomIds = new ArrayList<>();
            for (Sys_apply apply : applys) {
                applyIds.add(apply.getId());
                roomIds.add(apply.getRoomId());
            }
            applyMapper.updateLessThanNowApplying(applyIds.toArray());
            roomMapper.updateLessThanNowRoom(roomIds.toArray());
        }
    }

    @Override
    public void updateLessThanNowApplyed() {
        //获取状态为待审批且结束使用时间小于当前时间的申请
        List<Sys_apply> applys = applyMapper.getLessThanNowApplyed();
        if (applys.size() > 0) {
            ArrayList<String> applyIds = new ArrayList<>();
            ArrayList<String> roomIds = new ArrayList<>();
            for (Sys_apply apply : applys) {
                applyIds.add(apply.getId());
                roomIds.add(apply.getRoomId());
            }
            applyMapper.updateLessThanNowApplyed(applyIds.toArray());
            roomMapper.updateLessThanNowRoom(roomIds.toArray());
        }
    }
}
