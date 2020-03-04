package com.ws.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bean.Sys_room;

import java.util.List;

public interface RoomService extends IService<Sys_room> {

    /**
     * 分页获取会议室列表
     * @param iPage 分页信息
     * @param roomCondition 查询关键字
     * @return List<Sys_room>
     */
    List<Sys_room> roomListByPage(Page<Sys_room> iPage, String roomCondition);

    boolean checkRoomStatus(String roomId);

}
