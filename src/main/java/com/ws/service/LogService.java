package com.ws.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bean.Sys_log;

public interface LogService extends IService<Sys_log> {
    /**
     * 新增日志
     * @param log 日志实体信息
     * @return boolean
     */
    boolean addLog(Sys_log log);
}
