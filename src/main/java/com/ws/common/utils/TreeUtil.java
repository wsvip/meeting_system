package com.ws.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ws.bean.Sys_Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成树形结构工具类
 */
public class TreeUtil {


    /**
     * 构建树形结构
     *
     * @param permissionList
     * @return
     */
    public static List<Sys_Permission> buildTree(List<Sys_Permission> permissionList) {
        ArrayList<Sys_Permission> sys_permissions = new ArrayList<>();
        for (Sys_Permission permission : permissionList) {
            //判断是不是父节点
            if (permission.getHasChildren()&&permission.getPid().equals("-1")) {
                sys_permissions.add(findChildren(permission, permissionList));
            }
        }
        return sys_permissions;
    }

    /**
     * 递归查询子节点
     *
     * @param sys_permission
     * @param permissionList
     * @return
     */
    public static Sys_Permission findChildren(Sys_Permission sys_permission, List<Sys_Permission> permissionList) {
        ArrayList<Sys_Permission> children = new ArrayList<>();
        for (Sys_Permission permission : permissionList) {
            //查找sys_permission的子节点
            if (sys_permission.getId().equals(permission.getPid())) {
                children.add(findChildren(permission, permissionList));
                sys_permission.setChildren(children);
            }
        }

        return sys_permission;
    }

    /**
     * 根据父节点寻找子节点，并返回树形结构json串
     * @param jsonObj 父节点
     * @param jsonArray 节点集合
     * @return
     */
    public static JSONArray buildJsonTree(JSONObject jsonObj, JSONArray jsonArray){
        JSONArray treeData = new JSONArray();
        treeData.add(findChildrenInJson(jsonObj,jsonArray));
        return treeData;
    }

    private static JSONObject findChildrenInJson(JSONObject jsonObj, JSONArray jsonArray) {
        JSONArray jsonArr = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = (JSONObject) jsonArray.get(i);
            //如果拥有子节点
            if (jsonObj.get("id").equals(obj.get("pid"))){
                jsonArr.add(findChildrenInJson(obj,jsonArray));
                jsonObj.put("children",jsonArr);
            }
        }

        return jsonObj;
    }

}
