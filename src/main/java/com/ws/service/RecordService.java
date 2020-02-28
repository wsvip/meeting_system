package com.ws.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bean.Sys_record;

import java.util.HashMap;
import java.util.List;

public interface RecordService extends IService<Sys_record> {
    /**
     * 分页获取当前登录用户的会议记录
     * @param iPage 分页
     * @param recordCondition 关键字
     * @param userId 用户id
     * @return List<Sys_record>
     */
    List<Sys_record> recordListByPage(Page<Sys_record> iPage, String recordCondition,String userId);

    /**
     * 获取参会人员姓名集合
     * @param recordId 会议记录id
     * @return List<String>
     */
    HashMap<String, String> attendNamesListByRecordId(String recordId);

    /**
     * 获取会务专员姓名集合
     * @param recordId 会议记录id
     * @return List<String>
     */
    HashMap<String, String> commissionerNamesListByRecordId(String recordId);
}
