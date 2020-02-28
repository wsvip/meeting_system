package com.ws.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bean.Sys_record;
import com.ws.mapper.RecordMapper;
import com.ws.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Sys_record> implements RecordService {
    @Autowired
    private RecordMapper recordMapper;

    @Override
    public List<Sys_record> recordListByPage(Page<Sys_record> iPage, String recordCondition,String userId) {
        List<Sys_record> list=recordMapper.recordListByPage(iPage,recordCondition,userId);
        return list;
    }

    @Override
    public HashMap<String, String> attendNamesListByRecordId(String recordId) {
        List<Map<String, Object>> attendNames=recordMapper.attendNamesListByRecordId(recordId);
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (Map<String, Object> attendName : attendNames) {
            ids.add(attendName.get("userId").toString());
            names.add(attendName.get("nickname").toString());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("ids",String.join(",",ids));
        map.put("names",String.join(",",names));
        return map;
    }

    @Override
    public HashMap<String, String> commissionerNamesListByRecordId(String recordId) {
        List<Map<String, Object>> commissionerNames=recordMapper.commissionerNamesListByRecordId(recordId);
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (Map<String, Object> attendName : commissionerNames) {
            ids.add(attendName.get("userId").toString());
            names.add(attendName.get("nickname").toString());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("ids",String.join(",",ids));
        map.put("names",String.join(",",names));
        return map;
    }
}
