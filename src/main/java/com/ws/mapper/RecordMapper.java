package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.bean.Sys_record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RecordMapper extends BaseMapper<Sys_record> {

    List<Sys_record> recordListByPage(Page<Sys_record> iPage, @Param("recordCondition")String recordCondition,@Param("userId") String userId);

    List<Map<String,Object>> attendNamesListByRecordId(String recordId);


    List<Map<String,Object>> commissionerNamesListByRecordId(String recordId);
}
