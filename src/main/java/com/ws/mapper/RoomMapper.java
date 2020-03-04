package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.bean.Sys_room;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMapper extends BaseMapper<Sys_room> {

    /**
     * 分页获取会议室列表
     * @param iPage 分页信息
     * @param roomCondition 查询关键字
     * @return  List<Sys_room>
     */
    List<Sys_room> roomListByPage(Page<Sys_room> iPage, @Param("roomCondition")String roomCondition);

    void updateLessThanNowRoom(@Param("roomIds") Object[] roomIds);

    Integer checkRoomStatus(String roomId);
}
