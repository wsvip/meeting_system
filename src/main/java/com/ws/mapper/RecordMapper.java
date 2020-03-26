package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.bean.Sys_record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 会议记录mapper
 */
@Repository
public interface RecordMapper extends BaseMapper<Sys_record> {
    /**
     * 分页获取当前登录用户的会议记录
     * @param iPage 分页
     * @param recordCondition 关键字
     * @param userId 用户id
     * @return List<Sys_record>
     */
    List<Sys_record> recordListByPage(Page<Sys_record> iPage, @Param("recordCondition")String recordCondition,@Param("userId") String userId);
    /**
     * 获取参会人员姓名集合
     * @param recordId 会议记录id
     * @return List<String>
     */
    List<Map<String,Object>> attendNamesListByRecordId(String recordId);

    /**
     * 获取会务专员姓名集合
     * @param recordId 会议记录id
     * @return List<String>
     */
    List<Map<String,Object>> commissionerNamesListByRecordId(String recordId);
}
