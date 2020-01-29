package com.ws.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bean.Sys_User;
import com.ws.mapper.UserMapper;
import com.ws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,Sys_User> implements UserService  {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Sys_User findByUsername(String username) {
        //return userMapper.findByUsername(username);
        QueryWrapper<Sys_User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        Sys_User user = userMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public int saveUser(Sys_User sysUser) {
        int insert = userMapper.insert(sysUser);
        return insert;
    }

    @Override
    public boolean checkUsername(String username) {
        QueryWrapper<Sys_User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        Sys_User user = userMapper.selectOne(wrapper);
        if(null==user){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEmail(String email) {
        QueryWrapper<Sys_User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        Sys_User user = userMapper.selectOne(wrapper);
        if(null==user){
          return true;
        }
        return false;
    }

    @Override
    public boolean checkPhone(String tellphone) {
        QueryWrapper<Sys_User> wrapper = new QueryWrapper<>();
        wrapper.eq("tellphone", tellphone);
        Sys_User user = userMapper.selectOne(wrapper);
        if(null==user){
            return true;
        }
        return false;
    }


    @Override
    public void updateUser(Sys_User user) {
        //userMapper.updateUserWhenLogin(loginIp,loginCount,loginAt,id);
        userMapper.updateById(user);
    }

    @Override
    public List<Sys_User> userListByPage(Page<Sys_User> iPage,String condition) {
        List<Sys_User>userList=userMapper.userListByPage(iPage,condition);
        return userList;
    }

    @Override
    public int delUserData(String userId) {
        int delCount = userMapper.delUserAndUserRole(userId);
        return delCount;
    }

    @Override
    public Sys_User findByUserId(String userId) {
        Sys_User sys_user = userMapper.selectById(userId);
        return sys_user;
    }

    @Override
    public int editUserByUserId(Sys_User user) {
        int i = userMapper.updateById(user);
        return i;
    }

    @Override
    public List<Sys_User> getAssUserData(String roleId) {
        List<Sys_User>  userList=userMapper.getAssUserData(roleId);
        return userList;
    }

    @Override
    public List<Sys_User> getAssedUserData(String roleId) {
        List<Sys_User> userList=userMapper.getAssedUserData(roleId);
        return userList;
    }
}
