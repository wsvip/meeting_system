package com.ws.controller.sys;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.annotation.SLog;
import com.ws.bean.Sys_User;
import com.ws.common.utils.ResultUtil;
import com.ws.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v1/api/sys/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequiresPermissions("sys.user")
    @RequestMapping("/index")
    public Object index(){
        return "sys/user/index";
    }

    @RequiresPermissions("sys.user")
    @RequestMapping("/userListData")
    @ResponseBody
    @SLog(operate = "查看用户列表")
    public Object userListData(@RequestParam("page")int page,@RequestParam("limit")int limit,@RequestParam(value = "userCondition",required = false) String userCondition){
        Page<Sys_User> iPage = new Page<>(page,limit);
        List<Sys_User> userlist = userService.userListByPage(iPage,userCondition);
        int count = userService.count();
        return ResultUtil.layuiPageData(0,null,count,userlist);
    }

    @RequiresPermissions("sys.user")
    @RequestMapping("/addUser")
    public Object addUser(){
        return "sys/user/add";
    }


    /**
     * @Author:  WS-
     * @param username
     * @param password
     * @param email
     * @param nickname
     * @return:
     * @Date:    2019/4/22  10:52
     * @Description:
     */
    @RequiresPermissions("sys.user.add")
    @SLog(operate = "新增用户")
    @RequestMapping(value = "/addUserDo", method = RequestMethod.POST)
    @ResponseBody
    public Object addUser(String username, String password, String email, String nickname,String tellphone) {
        Sys_User sysUser = new Sys_User();
        sysUser.setUsername(username);
        SecureRandomNumberGenerator srng = new SecureRandomNumberGenerator();
        String salt = srng.nextBytes().toBase64();
        String pwd = new Sha256Hash(password, salt, 1024).toBase64();
        sysUser.setSalt(salt);
        sysUser.setTellphone(tellphone);
        sysUser.setPassword(pwd);
        sysUser.setEmail(email);
        sysUser.setNickname(nickname);
        userService.save(sysUser);
        return ResultUtil.success(null,"新增用户成功！");
    }

    @RequestMapping(value = "/findUserByUsername",method = RequestMethod.POST)
    public String findUserByUsername(String username, Map<String, Object> map) {
        Sys_User sysUser = userService.findByUsername(username);
        map.put("userkey", sysUser);
        return "/hello";
    }

    /**
     * 删除单条用户数据
     * @param userId 用户id
     * @return map
     */
    @RequiresPermissions("sys.user.delete")
    @SLog(operate = "删除用户")
    @RequestMapping(value = "/delUserData",method = RequestMethod.POST)
    @ResponseBody
    public Object delUserData(String userId){
        int delCount=userService.delUserData(userId);
        if (delCount>0){
            return ResultUtil.success(delCount,"删除成功");
        }else {
            return ResultUtil.error(1,"删除失败");
        }

    }


    /**
     * 修改用户
     * @param userId
     * @param request
     * @return
     */
    @RequiresPermissions("sys.user")
    @RequestMapping(value = "/editUser")
    public Object editUser(@RequestParam("userId") String userId, HttpServletRequest request){
        Sys_User user=userService.findByUserId(userId);
        request.setAttribute("sysUser",user);
        return "sys/user/edit";
    }

    @RequiresPermissions("sys.user.edit")
    @SLog(operate = "修改用户")
    @RequestMapping(value = "/editUserDo",method = RequestMethod.POST)
    @ResponseBody
    public Object editUserDo(Sys_User user){
        System.err.println(user);
        int flag=userService.editUserByUserId(user);
        if (flag>0) {
            return ResultUtil.success(flag, "修改成功！");
        }else{
            return ResultUtil.error(1,"修改失败!");
        }
    }
}
