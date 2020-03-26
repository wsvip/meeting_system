package com.ws.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bean.Sys_Permission;

import java.util.ArrayList;
import java.util.List;

public interface PermissionService extends IService<Sys_Permission> {
    /**
     * 获取权限列表
     * @return List<Sys_Permission>
     */
    List<Sys_Permission> getAllPermission();

    /**
     * 根据权限id删除权限
     * @param permId 权限id
     * @return int
     */
    int delPermById(String permId);

    /**
     * 根据权限id，批量删除权限
     * @param ids 权限id集合
     * @return int
     */
    int delPermBath(ArrayList<String> ids);

    /**
     * 根据角色id获取权限列表
     * @param roleId  角色id
     * @return List<Sys_Permission>
     */
    List<Sys_Permission> findPermsByRoleId(String roleId);
}
