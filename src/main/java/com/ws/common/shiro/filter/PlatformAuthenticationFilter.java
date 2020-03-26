package com.ws.common.shiro.filter;

import com.ws.common.shiro.token.PlatformToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class PlatformAuthenticationFilter extends FormAuthenticationFilter  {
    private String captchaParam="platformCaptcha";

    public String getCaptchaParam() {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }
    protected String  getCaptcha(ServletRequest servletRequest){
        return WebUtils.getCleanParam(servletRequest,captchaParam);
    }

    protected AuthenticationToken createToken(HttpServletRequest request){
        String username=getUsername(request);
        String password=getPassword(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        PlatformToken platformToken = new PlatformToken(username, password, rememberMe, host, captcha);
        return platformToken;
    }



}
