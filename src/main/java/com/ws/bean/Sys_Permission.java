package com.ws.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name="SYS_PERMISSION")
@TableName("sys_permission")
public class Sys_Permission implements Serializable {
    //@Id
    @TableId(type = IdType.UUID)
    private String id;
    //@Column(nullable = false)
    private String name;
    //@Column(nullable = false)
    private String resourceType;
    //@Column(nullable = false)
    private String url;
    //@Column(nullable = false)
    private String permission;
    private String icon;
    private Boolean isHasChildren = Boolean.FALSE;
    private String pid;
    //@Column(nullable = false)
    private Boolean disabled = Boolean.FALSE;
    //@ManyToMany
    //@JoinTable(name="SYS_ROLE_PER", joinColumns = {@JoinColumn(name = "per_id")},inverseJoinColumns = {@JoinColumn(name = "role_id")})

    @TableField(exist = false)
    private List<Sys_Permission> children = new ArrayList<>();

    @TableField(exist = false)
    private List<Sys_Role> roleList = new ArrayList<>();

    @TableField(exist = false)
    private Boolean checked = Boolean.FALSE;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public List<Sys_Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Sys_Permission> children) {
        this.children = children;
    }

    public Boolean getHasChildren() {
        return isHasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        isHasChildren = hasChildren;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<Sys_Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Sys_Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "Sys_Permission{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", url='" + url + '\'' +
                ", permission='" + permission + '\'' +
                ", isHasChildren=" + isHasChildren +
                ", pid='" + pid + '\'' +
                ", disabled=" + disabled +
                ", children=" + children +
                ", roleList=" + roleList +
                '}';
    }
}
