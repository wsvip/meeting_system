package com.ws.controller.sys;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.annotation.SLog;
import com.ws.bean.Sys_Role;
import com.ws.bean.Sys_User;
import com.ws.common.utils.ResultUtil;
import com.ws.common.utils.ShiroUtil;
import com.ws.service.PermissionService;
import com.ws.service.RoleService;
import com.ws.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/v1/api/sys/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @RequiresPermissions("sys.role")
    @RequestMapping(value = "/index")
    public Object index(){
        return "sys/role/index";
    }

    /**
     * 分页查询角色列表
     * @param page 当前页
     * @param limit 每页显示数量
     * @param roleCondition 查询关键字
     * @return
     */
    @SLog(operate = "查看角色列表")
    @RequiresPermissions("sys.role")
    @RequestMapping("/roleListData")
    @ResponseBody
    public Object roleListData(@RequestParam("page")int page, @RequestParam("limit")int limit, @RequestParam(value = "roleCondition",required = false) String roleCondition){
        Page<Sys_Role> rolePage = new Page<>(page,limit);
        List<Sys_Role> roleListData=roleService.roleListDataByPage(rolePage,roleCondition);
        int count =roleService.count();
        return ResultUtil.layuiPageData(0,null,count,roleListData);
    }

    /**
     * 跳转到新增角色界面
     * @return
     */
    @RequestMapping("/addRole")
    @RequiresPermissions("sys.role.add")
    public String addRole(){
        return "sys/role/add";
    }

    /**
     * 新增角色
     * @param role =需要新增的角色对象
     * @return
     */
    @SLog(operate = "新增角色")
    @RequestMapping(value="/addRoleDo",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.role.add")
    public Object addRoleDo(Sys_Role role){
        boolean flag = roleService.save(role);
        if (flag){
            return ResultUtil.success(null,"新增成功");
        }else{
            return ResultUtil.error(1,"删除失败");
        }
    }

    /**
     * 跳转到修改角色界面，并返回角色对象到界面中
     * @param roleId 角色id
     * @param request
     * @return 跳转页面
     */
    @RequestMapping("/editRole")
    @RequiresPermissions("sys.role.edit")
    public String editRole(@RequestParam("roleId") String roleId,HttpServletRequest request){
        Sys_Role role=roleService.getRoleByRoleId(roleId);
        request.setAttribute("sysRole",role);
        return "sys/role/edit";

    }

    /**
     * 修改角色信息
     * @param role 前端提交的角色对象
     * @return 成功或失败信息
     */
    @SLog(operate = "修改角色")
    @RequestMapping(value = "/editRoleDo",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.role.edit")
    public Object editRoleDo(Sys_Role role){
        boolean flag = roleService.updateById(role);
        if (flag){
            return ResultUtil.success(flag,"修改成功");
        }else{
            return ResultUtil.error(1,"修改失败");
        }
    }

    /**
     * 根据角色id删除角色，并删除角色-用户中间表中信息
     * @param roleId 角色id
     * @return 成功或失败信息
     */
    @SLog(operate = "删除角色")
    @RequestMapping(value = "/delRole",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.role.del")
    public Object delRole(String roleId){
        roleService.delRoleByRoleId(roleId);
        return ResultUtil.success(null,"删除成功");
    }

    /**
     * 跳转到分配用户界面
     * @param roleId
     * @param request
     * @return
     */
    @RequestMapping("/assUser")
    @RequiresPermissions("sys.role.user")
    public String assUser(@RequestParam("roleId")String roleId,HttpServletRequest request){
        request.setAttribute("roleId",roleId);
        return "sys/role/assUser";
    }

    /**
     * 获取未分配的用户集合
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/getAssUserData",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.role.user")
    public Object getAssUserData(@RequestParam("roleId")String roleId){
        List<Sys_User> userList= userService.getAssUserData(roleId);
        return userList;
    }

    /**
     * 获取已分配的用户集合
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/getAssedUserData",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("sys.role.user")
    public Object getAssedUserData(@RequestParam("roleId")String roleId){
        List<Sys_User> userList=userService.getAssedUserData(roleId);
        return userList;
    }

    @RequestMapping(value = "/saveAssUser",method = RequestMethod.POST)
    @ResponseBody
    @SLog(operate = "角色-分配用户")
    @RequiresPermissions("sys.role.user")
    public Object saveAssUser(@RequestParam("ids")String[] ids,@RequestParam("roleId")String roleId){
        int count=roleService.saveAssUser(ids,roleId);
        ShiroUtil.clearPermisson();
        return ResultUtil.success(count,"分配成功");
    }


    /**
     * 跳转到分配权限界面
     * @param roleId
     * @param request
     * @return
     */
    @RequestMapping("/assPerm")
    @RequiresPermissions("sys.role.perm")
    public String assPerm(@RequestParam("roleId")String roleId,HttpServletRequest request){
        request.setAttribute("roleId",roleId);
        return "sys/role/assPerm";
    }

    /**
     * 获取权限列表并回显该角色已有的权限
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/feedbackTree",method = RequestMethod.POST)
    @RequiresPermissions("sys.role.perm")
    public Object tree(@RequestParam("roleId")String roleId){
        JSONArray permissionList =roleService.getAssPermTree(roleId);
        return permissionList;
    }

    @RequestMapping(value = "/saveAssPerm",method = RequestMethod.POST)
    @ResponseBody
    @SLog(operate = "角色-分配权限")
    @RequiresPermissions("sys.role.perm")
    public Object saveAssPerm(@RequestParam("ids")String[] ids,@RequestParam("roleId")String roleId){
        int count=roleService.saveAssPerm(ids,roleId);
        ShiroUtil.clearPermisson();
        return ResultUtil.success(null,"保存成功");
    }
}
