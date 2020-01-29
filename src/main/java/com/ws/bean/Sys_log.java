package com.ws.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
@TableName("sys_log")
public class Sys_log implements Serializable {
    private final static long serialVersionUID=1l;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String nickname;

    private String ip;

    private String time;

    private String url;

    private String method;

    private String operate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Sys_log() {
    }

    public Sys_log(String id, String username, String nickname, String ip, String time, String url, String method, String operate) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.ip = ip;
        this.time = time;
        this.url = url;
        this.method = method;
        this.operate = operate;
    }
}
