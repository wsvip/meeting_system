package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.bean.Sys_apply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyMapper extends BaseMapper<Sys_apply> {
    /**
     * 分页获取申请列表
     * @param iPage 分页
     * @param applyCondition 关键字
     * @return List<Sys_apply>
     */
    List<Sys_apply> applyListByPage(Page<Sys_apply> iPage,@Param("applyCondition") String applyCondition,@Param("userId")String userId);

    boolean updateRoom(@Param("id")String id, @Param("roomName")String roomName, @Param("roomNo")String roomNo);

    boolean updateApplyStatus(@Param("roomId")String roomId, @Param("status")int status);

    List<Sys_apply> pendingListByPage(Page<Sys_apply> iPage, @Param("applyCondition")String applyCondition,@Param("userId") String userId);

    List<Sys_apply> applyedListByPage(Page<Sys_apply> iPage, @Param("userId")String userId);

    List<Sys_apply> getLessThanNowApplying();

    void updateLessThanNowApplying(@Param("applyIds")Object[] applyIds);

    List<Sys_apply> getLessThanNowApplyed();

    void updateLessThanNowApplyed(@Param("applyIds")Object[] toArray);

}
