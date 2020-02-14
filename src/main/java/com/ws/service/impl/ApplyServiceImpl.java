package com.ws.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bean.Sys_apply;
import com.ws.mapper.ApplyMapper;
import com.ws.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Sys_apply> implements ApplyService {
    @Autowired
    private ApplyMapper applyMapper;

    @Override
    public List<Sys_apply> applyListByPage(Page<Sys_apply> iPage, String applyCondition,String userId) {
        List<Sys_apply> applyList = applyMapper.applyListByPage(iPage, applyCondition,userId);
        return applyList;
    }

    @Override
    public boolean updateRoom(String id, String roomName, String roomNo) {
        boolean flag = applyMapper.updateRoom(id, roomName, roomNo);
        return flag;
    }

    @Override
    public boolean updateApplyStatus(String roomId, int status) {
        boolean flag =applyMapper.updateApplyStatus(roomId, status);
        return flag;
    }

    @Override
    public List<Sys_apply> pendingListByPage(Page<Sys_apply> iPage, String applyCondition, String userId) {
        List<Sys_apply> pendingList =applyMapper.pendingListByPage(iPage,applyCondition,userId);
        return pendingList;
    }
}
