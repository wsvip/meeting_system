package com.ws.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bean.Sys_record_commissioner;
import com.ws.mapper.CommissionerMapper;
import com.ws.service.CommissionerService;
import org.springframework.stereotype.Service;

@Service
public class CommissionerServiceImpl extends ServiceImpl<CommissionerMapper, Sys_record_commissioner> implements CommissionerService {
}
