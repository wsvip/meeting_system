package com.ws.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bean.Sys_apply;

import java.util.List;

public interface ApplyService extends IService<Sys_apply> {
    /**
     * 分页获取申请列表
     * @param iPage 分页
     * @param applyCondition 关键字
     * @return
     */
    List<Sys_apply> applyListByPage(Page<Sys_apply> iPage, String applyCondition,String userId);

    /**
     * 修改申请表中会议室的信息
     * @param id 会议室id
     * @param roomName 会议室名称
     * @param roomNo 会议室编号
     * @return boolean
     */
    boolean updateRoom(String id, String roomName, String roomNo);

    /**
     * 修改申请表申请状态
     * @param roomId 会议室Id
     * @param status
     * @return boolean
     */
    boolean updateApplyStatus(String roomId, int status);

    /**
     * 分页获取当前用户待办事项数据
     * @param iPage 分页信息
     * @param applyCondition 关键字
     * @param id  当前用户id
     * @return List<Sys_apply>
     */
    List<Sys_apply> pendingListByPage(Page<Sys_apply> iPage, String applyCondition, String id);

    /**
     * 分页获取当前用户已通过申请的列表
     * @param iPage 分页
     * @param userId 当前登录用户id
     * @return List<Sys_apply>
     */
    List<Sys_apply> applyedListByPage(Page<Sys_apply> iPage, String userId);

    /**
     * 设置结束时间小于当前时间的待审批申请、状态为申请超时
     */
    void updateLessThanNowApplying();

    /**
     * 设置结束时间小于当前时间的已通过审批申请 状态为结束使用
     */
    void updateLessThanNowApplyed();
}
