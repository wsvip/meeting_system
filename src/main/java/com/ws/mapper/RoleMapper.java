package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.bean.Sys_Permission;
import com.ws.bean.Sys_Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoleMapper extends BaseMapper<Sys_Role> {
    /**
     * 分因查询角色列表
     * @param rolePage 分页信息
     * @param roleCondition 关键字
     * @return List<Sys_Role>
     */
    List<Sys_Role> roleListDataByPage(@Param("rolePage") Page<Sys_Role> rolePage, @Param("roleCondition") String roleCondition);

    int delRoleAndUserRole(String roleId);
    @Transactional
    int saveAssUser(@Param("ids") List<String> ids,@Param("roleId") String roleId);

    int delAssUserByRoleId(String roleId);

    List<Sys_Permission> getPermByRoleId(String roleId);

    int saveAssPerm(@Param("idList") List<String> idList,@Param("roleId") String roleId);

    int deleteAssPerm(String roleId);

    List<Sys_Role> findRolesByUserId(String userId);

}
