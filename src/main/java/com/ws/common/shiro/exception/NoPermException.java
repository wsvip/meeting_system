package com.ws.common.shiro.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class NoPermException {
    //@ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public String handleShiroException(Exception e) {
        return "noPermission";
    }

    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationException(Exception e) {
        return "权限认证失败";
    }
}
