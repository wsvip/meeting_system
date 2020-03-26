package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.bean.Sys_room;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会议室mapper接口
 */
@Repository
public interface RoomMapper extends BaseMapper<Sys_room> {

    /**
     * 分页获取会议室列表
     * @param iPage 分页信息
     * @param roomCondition 查询关键字
     * @return  List<Sys_room>
     */
    List<Sys_room> roomListByPage(Page<Sys_room> iPage, @Param("roomCondition")String roomCondition);

    /**
     * 更新已超过使用时间的会议室状态为空闲状态
     * @param roomIds 会议室id集合
     */
    void updateLessThanNowRoom(@Param("roomIds") Object[] roomIds);

    /**
     * 检查会议室状态
     * @param roomId 会议室id
     * @return int
     */
    int checkRoomStatus(String roomId);
}
