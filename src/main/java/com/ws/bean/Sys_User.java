package com.ws.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ws.common.utils.StringUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//@Entity
//@Table(name = "SYS_USER")
@TableName("sys_user")
public class Sys_User implements Serializable {
    //@Id
   // @GenericGenerator(name = "userId", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
   // @GeneratedValue(generator = "userId")
    @TableId(type = IdType.UUID)
    private String id;
    //@Column(nullable = false, unique = true)
    private String username;
    //@Column(nullable = false)
    private String password;
    //@Column(nullable = false)
    private String salt;
    //@Column(nullable = true)
    private String nickname;
    //@Column(nullable = true, unique = true)
    private String email;
    //@Column(nullable = true)
    private String tellphone;
    //@Column(nullable = true)
    private String address;
    //@Column(nullable = true)
    private String loginIp;
    //@Column(nullable = true)
    private Integer loginAt;
    //@Column(nullable = true)
    private Integer loginCount;
    //@Column(nullable = true)
    public Boolean disabled=Boolean.FALSE;
    //@ManyToMany(fetch = FetchType.EAGER)
    //@JoinTable(name = "SYS_USER_ROLE",joinColumns = {@JoinColumn(name = "user_id")},inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @TableField(exist = false)
    private List<Sys_Role> roles;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTellphone() {
        return tellphone;
    }

    public void setTellphone(String tellphone) {
        this.tellphone = tellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Integer getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(Integer loginAt) {
        this.loginAt = loginAt;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<Sys_Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Sys_Role> roles) {
        this.roles = roles;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
