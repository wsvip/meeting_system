package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.bean.Sys_Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper extends BaseMapper<Sys_Permission> {
    List<Sys_Permission> findPermsByRoleId(String roleId);

}
