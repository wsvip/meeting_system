package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.bean.Sys_apply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 申请单mapper
 */
@Repository
public interface ApplyMapper extends BaseMapper<Sys_apply> {
    /**
     * 分页获取申请列表
     * @param iPage 分页
     * @param applyCondition 关键字
     * @return List<Sys_apply>
     */
    List<Sys_apply> applyListByPage(Page<Sys_apply> iPage,@Param("applyCondition") String applyCondition,@Param("userId")String userId);

    /**
     * 修改申请表中会议室的信息
     * @param id 会议室id
     * @param roomName 会议室名称
     * @param roomNo 会议室编号
     * @return boolean
     */
    boolean updateRoom(@Param("id")String id, @Param("roomName")String roomName, @Param("roomNo")String roomNo);

    /**
     * 修改申请表申请状态
     * @param roomId 会议室Id
     * @param status
     * @return boolean
     */
    boolean updateApplyStatus(@Param("roomId")String roomId, @Param("status")int status);

    /**
     * 分页获取当前用户待办事项数据
     * @param iPage 分页信息
     * @param applyCondition 关键字
     * @param userId 当前用户id
     * @return List<Sys_apply>
     */
    List<Sys_apply> pendingListByPage(Page<Sys_apply> iPage, @Param("applyCondition")String applyCondition,@Param("userId") String userId);

    /**
     * 分页获取当前用户已通过申请的列表
     * @param iPage 分页
     * @param userId 当前登录用户id
     * @return List<Sys_apply>
     */
    List<Sys_apply> applyedListByPage(Page<Sys_apply> iPage, @Param("userId")String userId);

    /**
     * 获取状态为待审批且结束使用时间小于当前时间的申请
     * @return List<Sys_apply>
     */
    List<Sys_apply> getLessThanNowApplying();

    /**
     * 设置结束时间小于当前时间的待审批申请、状态为申请超时
     */
    void updateLessThanNowApplying(@Param("applyIds")Object[] applyIds);

    /**
     * 设置结束时间小于当前时间的已通过审批申请 状态为结束使用
     */
    void updateLessThanNowApplyed(@Param("applyIds")Object[] toArray);

    List<Sys_apply> getLessThanNowApplyed();
}
