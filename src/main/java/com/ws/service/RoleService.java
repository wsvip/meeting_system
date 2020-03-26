package com.ws.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bean.Sys_Permission;
import com.ws.bean.Sys_Role;

import java.util.List;

public interface RoleService extends IService<Sys_Role> {
    /**
     * 分因查询角色列表
     * @param rolePage 分页信息
     * @param roleCondition 关键字
     * @return List<Sys_Role>
     */
    List<Sys_Role> roleListDataByPage(Page<Sys_Role> rolePage, String roleCondition);

    /**
     * 根据角色id获取角色对象
     * @param roleId 角色id
     * @return 角色对象
     */
    Sys_Role getRoleByRoleId(String roleId);

    /**
     * 根据角色id删除角色
     * @param roleId 角色id
     */
    void delRoleByRoleId(String roleId);

    /**
     * 分配用户到角色中
     * @param ids 用户id集合
     * @return int
     */
    int saveAssUser(String[] ids,String roleId);

    /**
     * 获取权限树形结构，并以角色id作为回显选中标识
     * @param roleId 角色id
     * @return JSONArray
     */
    JSONArray getAssPermTree(String roleId);

    /**
     * 保存分配的权限
     * @param ids 权限id数组集合
     * @param roleId 角色id
     * @return int
     */
    int saveAssPerm(String[] ids, String roleId);

    /**
     * 根据用户id获取用户角色列表
     * @param userId 用户id
     * @return List<Sys_Role>
     */
    List<Sys_Role> findRolesByUserId(String userId);

}
