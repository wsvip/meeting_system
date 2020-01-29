package com.ws.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bean.Sys_Permission;
import com.ws.bean.Sys_Role;
import com.ws.common.utils.TreeUtil;
import com.ws.mapper.RoleMapper;
import com.ws.service.PermissionService;
import com.ws.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Sys_Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<Sys_Role> roleListDataByPage(Page<Sys_Role> rolePage, String roleCondition) {
        List<Sys_Role> roleListDataByPage = roleMapper.roleListDataByPage(rolePage, roleCondition);
        return roleListDataByPage;
    }

    @Override
    public Sys_Role getRoleByRoleId(String roleId) {
        Sys_Role role = roleMapper.selectById(roleId);
        return role;
    }

    @Override
    public void delRoleByRoleId(String roleId) {
        int delCount = roleMapper.delRoleAndUserRole(roleId);
    }

    @Override
    public int saveAssUser(String[] ids, String roleId) {
        List<String> idList = Arrays.asList(ids);
        int flag = roleMapper.delAssUserByRoleId(roleId);
        int count = roleMapper.saveAssUser(idList, roleId);
        return count;
    }

    @Override
    public JSONArray getAssPermTree(String roleId) {
        //获取权限树形结构
        List<Sys_Permission> permissionTreeData = permissionService.getAllPermission();
        //根据角色id获取角色所拥有的权限树形结构
        List<Sys_Permission> feedbackPermList = roleMapper.getPermByRoleId(roleId);
        JSONArray permissionTreeDataJson = (JSONArray) JSON.toJSON(permissionTreeData);
        JSONArray feedbackPermListJson = (JSONArray) JSON.toJSON(feedbackPermList);
        //装配复选框类型和状态到json中，并筛选该角色下已有的权限进行回显
        for (int j = 0; j < permissionTreeDataJson.size(); j++) {
            ArrayList<Map> maps = new ArrayList<>();
            HashMap<String, String> map = new HashMap<>();
            JSONObject permissionTreeDataJsonObj = (JSONObject) permissionTreeDataJson.get(j);
            for (int i = 0; i < feedbackPermListJson.size(); i++) {
                JSONObject feedbackJsonObj = (JSONObject) feedbackPermListJson.get(i);
                if (feedbackJsonObj.getString("id").equals(permissionTreeDataJsonObj.getString("id"))) {
                    map.put("type", "0");
                    map.put("checked", "1");
                    break;
                } else {
                    map.put("type", "0");
                    map.put("checked", "0");
                }
            }
            maps.add(map);
            permissionTreeDataJsonObj.put("checkArr", maps);
        }
        JSONArray treeJsonData = TreeUtil.buildJsonTree((JSONObject) permissionTreeDataJson.get(0), permissionTreeDataJson);
        return treeJsonData;
    }

    @Override
    public int saveAssPerm(String[] ids, String roleId) {
        //先删除之前的再添加
        int flag = roleMapper.deleteAssPerm(roleId);
        List<String> idList = Arrays.asList(ids);
        int count = 0;
        if (flag >= 0) {
            count = roleMapper.saveAssPerm(idList, roleId);
        }
        return count;
    }

    @Override
    public List<Sys_Role> findRolesByUserId(String userId) {
        List<Sys_Role> roles=roleMapper.findRolesByUserId(userId);
        if (null != roles && roles.size() > 0) {
            return roles;
        }
        return null;
    }
}
