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
    /**
     * 根据角色id删除角色
     * @param roleId 角色id
     * @return int
     */
    int delRoleAndUserRole(String roleId);
    /**
     * 分配用户到角色中
     * @param ids 用户id集合
     * @return int
     */
    @Transactional
    int saveAssUser(@Param("ids") List<String> ids,@Param("roleId") String roleId);

    /**
     * 根据角色id删除当前角色中的用户
     * @param roleId 角色id
     * @return int
     */
    int delAssUserByRoleId(String roleId);

    /**
     * 根据角色id获取权限集合
     * @param roleId 角色id
     * @return List<Sys_Permission>
     */
    List<Sys_Permission> getPermByRoleId(String roleId);

    /**
     * 保存已分配权限集合
     * @param idList 权限id集合
     * @param roleId 角色id
     * @return int
     */
    int saveAssPerm(@Param("idList") List<String> idList,@Param("roleId") String roleId);

    /**
     * 根绝角色id删除已分配权限
     * @param roleId 角色id
     * @return int
     */
    int deleteAssPerm(String roleId);

    /**
     * 根据用户id获取角色
     * @param userId 用户id
     * @return List<Sys_Role>
     */
    List<Sys_Role> findRolesByUserId(String userId);

}
