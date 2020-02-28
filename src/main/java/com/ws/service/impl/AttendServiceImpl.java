package com.ws.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bean.Sys_record_attend;
import com.ws.mapper.AttendMapper;
import com.ws.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendServiceImpl extends ServiceImpl<AttendMapper, Sys_record_attend> implements AttendService {
}
