package com.ws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.bean.Sys_Permission;
import com.ws.bean.Sys_User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
//public interface UserMapper extends JpaRepository<Sys_User, String> {
public interface UserMapper extends BaseMapper<Sys_User> {
    /**
     * 分页获取用户列表
     * @param iPage 分页
     * @param condition 关键字
     * @return  List<Sys_User>
     */
    List<Sys_User> userListByPage(@Param("iPage") Page<Sys_User> iPage,@Param("condition") String condition);

    /**
     * 根据用户id级联删除角色-用户管理表数据
     * @param userId 用户id
     * @return int
     */
    int delUserAndUserRole(String userId);

    /**
     * 根绝角色id获取未分配用户集合
     * @param roleId 角色id
     * @return List<Sys_User>
     */
    List<Sys_User> getAssUserData(String roleId);

    /**
     * 根据角色id获取已分配用户集合
     * @param roleId 角色id
     * @return List<Sys_User>
     */
    List<Sys_User> getAssedUserData(String roleId);

    /**
     * 获取用户权限菜单集合
     * @param userId 用户id
     * @return List<Sys_Permission>
     */
    List<Sys_Permission> getUserPermissionMenus(String userId);

    //void selectOne(QueryWrapper<Object> );

    //Sys_User findByUsername(String username);

    //Sys_User save(Sys_User sysUser);

    //@Query(value = "select username from Sys_User where  username=?1")
    //String checkUsername(String username);
    //@Query(value = "select email from Sys_User where email=?1")
    //String checkEmail(String email);
    //@Query(value="select tellphone from Sys_User where tellphone =?1")
    //String checkPhone(String tellphone);
    //@Modifying
    //@Transactional
    //@Query("update Sys_User u set u.loginIp=?1,u.loginCount=?2,u.loginAt=?3 where id=?4")
    //void updateUserWhenLogin(String loginIp, int loginCount, int loginAt, String id);
}
