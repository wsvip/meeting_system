package com.ws.controller;

import com.alibaba.fastjson.JSON;
import com.ws.annotation.SLog;
import com.ws.bean.Sys_Permission;
import com.ws.bean.Sys_User;
import com.ws.common.shiro.token.PlatformToken;
import com.ws.common.utils.ResultUtil;
import com.ws.common.utils.StringUtil;
import com.ws.common.websocket.WebSocketServer;
import com.ws.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author WS-
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;
    /**
     * @Method: login
     * @Author: WS
     * @Date: 2018/12/14 13:06
     * @return: java.lang.String
     * @Description: 跳转到登录页面
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String login(){
        Sys_User user = (Sys_User) SecurityUtils.getSubject().getPrincipal();
        if (null!=user){
            Subject subject = SecurityUtils.getSubject();

            return "sys/user/index";
        }else {
            return "login";
        }
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param request 请求体
     * @return 登录成功或失败信息
     */
    //@SLog(operate = "登录系统")
    @RequestMapping(value = "/doLogin",method=RequestMethod.POST)
    @ResponseBody
    public Object doLogin(String username,String password,HttpServletRequest request){
        try {
            PlatformToken token = new PlatformToken(username, password, false, StringUtil.getRemoteAddr(request), null);
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);
            subject.login(token);

            String socketId = null;
            List<Session> loginedSession = getLoginedSession(subject);
            if (loginedSession.size()>0){
                for (Session session : loginedSession) {
                    socketId= (String) session.getId();
                    session.stop();
                }
                try {
                    Map<Object, Object> map = ResultUtil.error(302, "检测到账号异地登录");
                    String msg =  JSON.toJSON(map).toString();
                    WebSocketServer.sendInfo(msg,socketId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Sys_User user= (Sys_User) subject.getPrincipal();
            String loginIp = StringUtil.getRemoteAddr(request);
            int count =user.getLoginCount()==null?0:user.getLoginCount();
            user.setLoginIp(loginIp);
            user.setLoginCount(count+1);
            user.setLoginAt((int)(System.currentTimeMillis()/1000));
            userService.updateUser(user);
            //获取用户权限菜单，用于登录后生成菜单
            List<Sys_Permission> menus=userService.getUserPermissionMenus(user.getId());
            return ResultUtil.success(menus,"登录成功，正在跳转页面");
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResultUtil.error(1,"用户名或密码错误");
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ResponseBody
    public void logout(){
        SecurityUtils.getSubject().logout();
    }

    /**
     * 遍历同一个账户的session
     * @param currentUser
     * @return
     */
    private List<Session> getLoginedSession(Subject currentUser) {
        DefaultWebSecurityManager manager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) manager.getSessionManager();
        Collection<Session> list = sessionManager.getSessionDAO().getActiveSessions();
        List<Session> loginedList = new ArrayList<Session>();
        Sys_User loginUser = (Sys_User) currentUser.getPrincipal();
        for (Session session : list) {
            Subject s = new Subject.Builder().session(session).buildSubject();
            if (s.isAuthenticated()) {
                Sys_User user = (Sys_User) s.getPrincipal();
                if (user.getUsername().equalsIgnoreCase(loginUser.getUsername())) {
                    if (!session.getId().equals(currentUser.getSession().getId())) {
                        loginedList.add(session);
                    }
                }
            }
        }
        return loginedList;
    }

}
