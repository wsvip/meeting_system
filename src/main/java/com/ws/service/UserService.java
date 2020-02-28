package com.ws.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bean.Sys_Permission;
import com.ws.bean.Sys_Role;
import com.ws.bean.Sys_User;

import java.util.List;


public interface UserService extends IService<Sys_User> {
    Sys_User findByUsername(String username);

    int saveUser(Sys_User sysUser);
    /**
     * @Author: WS
     * @Date: 2018/12/5 19:05
     * @Params: username
     * @return: boolean
     * @Description: 校验用户名是否存在，存在返回true，不存在返回false
     *
     */
    boolean checkUsername(String username);
    /**
     * @Author: WS
     * @Date: 2018/12/5 19:05
     * @Params: email
     * @return: boolean
     * @Description: 校验邮箱是否存在，存在返回false，不存在返回true
     *
     */
    boolean checkEmail(String email);

     /**
     * @Author: WS
     * @Date: 2018/12/5 19:13
     * @Params: tellphone
     * @return:  boolean
     * @Description: 校验手机号是否存在，存在返回false，不存在返回true
     */
     boolean checkPhone(String tellphone);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(Sys_User user);

    /**
     * 分页查询用户信息
     * @param iPage 分页对象
     * @param condition  关键字
     * @return  List<Sys_User>
     */
    List<Sys_User> userListByPage(Page<Sys_User> iPage,String condition);

    /**
     * 根据用户id删除用户
     * @param userId
     * @return int
     */
    int delUserData(String userId);

    /**
     * 根据用户id查找用户
     * @param userId
     * @return Sys_User
     */
    Sys_User findByUserId(String userId);

    /**
     * 根据用户id更新用户
     * @param user
     * @return
     */
    int editUserByUserId(Sys_User user);

    /**
     * 根据角色id获取该角色之外的用户集合
     * @param roleId
     * @return  List<Sys_User>
     */
    List<Sys_User> getAssUserData(String roleId);

    /**
     * 根据角色id获取该角色下的用户集合
     * @param roleId
     * @return List<Sys_User>
     */
    List<Sys_User> getAssedUserData(String roleId);

    /**
     * 根据用户id获取用户权限菜单，用于登录成功后生成菜单
     * @param userId 用户id
     * @return List<Sys_Permission>
     */
    List<Sys_Permission> getUserPermissionMenus(String userId);
}
