package com.ws.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bean.Sys_Permission;

import java.util.ArrayList;
import java.util.List;

public interface PermissionService extends IService<Sys_Permission> {
    /**
     * 获取权限列表
     * @return
     */
    List<Sys_Permission> getAllPermission();

    int delPermById(String permId);

    int delPermBath(ArrayList<String> ids);

    /**
     * 根据角色id获取权限列表
     * @param roleId  角色id
     * @return List<Sys_Permission>
     */
    List<Sys_Permission> findPermsByRoleId(String roleId);
}
