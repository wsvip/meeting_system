package com.ws.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bean.Sys_room;
import com.ws.mapper.RoomMapper;
import com.ws.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Sys_room> implements RoomService {
    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<Sys_room> roomListByPage(Page<Sys_room> iPage, String roomCondition) {
        List<Sys_room> roomList= roomMapper.roomListByPage(iPage,roomCondition);
        return roomList;
    }

    @Override
    public boolean checkRoomStatus(String roomId) {
        int status=roomMapper.checkRoomStatus(roomId);
        if (status == 2) {
            return true;
        }
        return false;
    }
}
