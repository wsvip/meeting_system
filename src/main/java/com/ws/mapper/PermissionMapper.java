package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.bean.Sys_Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限mapper
 */
@Repository
public interface PermissionMapper extends BaseMapper<Sys_Permission> {
    /**
     * 根据角色id获取权限列表
     * @param roleId  角色id
     * @return List<Sys_Permission>
     */
    List<Sys_Permission> findPermsByRoleId(String roleId);

}
