package com.ws.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bean.Sys_log;
import com.ws.mapper.LogMapper;
import com.ws.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Sys_log> implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public boolean addLog(Sys_log log) {
        int flag = logMapper.insert(log);
        return flag > 0;
    }
}
