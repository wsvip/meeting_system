package com.ws.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//@Entity
//@Table(name="SYS_ROLE")
@TableName("sys_role")
public class Sys_Role implements Serializable {
    //@Id
    @TableId(type = IdType.UUID)
    private String id;
    //@Column(nullable = true)
    private String rolename;
    //@Column(nullable = true)
    private String role;
    //@Column(nullable = false)
    private String description;
    //@Column(nullable = true)
    private Boolean disabled=Boolean.FALSE;
    //@ManyToMany()
    //@JoinTable(name = "SYS_USER_ROLE",joinColumns = {@JoinColumn(name="role_id")},inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @TableField(exist = false)
    private List<Sys_User> userList;

    //@ManyToMany()
    //@JoinTable(name = "SYS_ROLE_PER",joinColumns = {@JoinColumn(name="role_id")},inverseJoinColumns = {@JoinColumn(name = "pre_id")})
    @TableField(exist = false)
    private List<Sys_Permission> permissions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<Sys_User> getUserList() {
        return userList;
    }

    public void setUserList(List<Sys_User> userList) {
        this.userList = userList;
    }

    public List<Sys_Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Sys_Permission> permissions) {
        this.permissions = permissions;
    }
}
