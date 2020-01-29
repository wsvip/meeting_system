package com.ws.common.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

public class PlatformToken extends UsernamePasswordToken {
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public PlatformToken(String username, String password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }
}
