package com.ws.controller.sys;

import com.ws.annotation.SLog;
import com.ws.bean.Sys_Permission;
import com.ws.common.utils.ResultUtil;
import com.ws.common.utils.Strings;
import com.ws.common.utils.TreeUtil;
import com.ws.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/v1/api/sys/perm")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    private ArrayList<String> ids;

    @RequestMapping("/index")
    public String index() {
        return "sys/perm/index";
    }

    /**
     * 获取权限列表树形结构
     *
     * @return
     */
    @SLog(operate = "查看权限列表")
    @RequestMapping("/permTreeTable")
    @ResponseBody
    public Object permTreeTable() {
        List<Sys_Permission> permList = permissionService.getAllPermission();
        return ResultUtil.success(permList, "ok");
    }

    /**
     * 跳转新增权限界面
     *
     * @return
     */
    @RequestMapping("/addPerm")
    public String addPerm() {
        return "sys/perm/add";
    }

    /**
     * 新增权限
     *
     * @param perm
     * @return
     */
    @SLog(operate = "新增权限")
    @RequestMapping(value = "/addPermDo", method = RequestMethod.POST)
    @ResponseBody
    public Object addPermDo(Sys_Permission perm) {
        //如果pid为null或为""空串时,将pid设置为顶级菜单-1
        if (Strings.isEmpty(perm.getPid())) {
            perm.setPid("-1");
        }
        boolean flag = permissionService.save(perm);
        if (flag) {
            return ResultUtil.success(null, "新增成功");
        } else {
            return ResultUtil.error(1, "新增失败");
        }
    }
    @SLog(operate = "删除权限")
    @RequestMapping(value = "/delPerm", method = RequestMethod.POST)
    @ResponseBody
    public Object delPerm(@RequestParam("permId") String permId) {
        //根据permId获取permission对象
        Sys_Permission permission = permissionService.getById(permId);
        //判断是不是父级
        int count;
        if (permission.getHasChildren()) {
            //是父级，使用递归方法获取children
            List<Sys_Permission> all = permissionService.getAllPermission();
            ids = new ArrayList<>();
            //把自己的id加入到id集合中
            ids.add(permId);
            //递归获取字节id
            getChildrenId(permission, all);
            count = permissionService.delPermBath(ids);
        } else {
            //不是父级，直接删除
            count = permissionService.delPermById(permId);
        }
        if (count > 0) {
            return ResultUtil.success(count, "删除成功");
        } else {
            return ResultUtil.error(1, "删除失败");
        }
    }

    /**
     * 跳转到修改权限界面，并获取要修改的权限对象
     *
     * @param permId
     * @param request
     * @return
     */
    @RequestMapping("/edit")
    public String edit(@RequestParam("permId") String permId, HttpServletRequest request) {
        Sys_Permission perm = permissionService.getById(permId);
        request.setAttribute("perm", perm);
        return "sys/perm/edit";
    }

    /**
     * 修改权限
     * @param permission
     * @return
     */
    @SLog(operate = "修改权限")
    @RequestMapping(value = "/editDo", method = RequestMethod.POST)
    @ResponseBody
    public Object editDo(Sys_Permission permission) {
        boolean flag = permissionService.updateById(permission);
        if (flag) {
            return ResultUtil.success(null, "修改成功");
        } else {
            return ResultUtil.error(1, "修改失败");
        }
    }

    /**
     * 获取权限树结构
     *
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public Object tree() {
        List<Sys_Permission> permissionList = permissionService.getAllPermission();
        List<Sys_Permission> sys_permissions = TreeUtil.buildTree(permissionList);
        return sys_permissions.get(0);
    }



    /**
     * 递归查询子节点id
     *
     * @param sys_permission
     * @param permissionList
     * @return
     */
    private Sys_Permission getChildrenId(Sys_Permission sys_permission, List<Sys_Permission> permissionList) {
        //List<Sys_Permission> children = sys_permission.getChildren();
        for (Sys_Permission permission : permissionList) {
            //查找sys_permission的子节点
            if (sys_permission.getId().equals(permission.getPid())) {
                getChildrenId(permission, permissionList);
                ids.add(permission.getId());
            }
        }
        return sys_permission;
    }

}
