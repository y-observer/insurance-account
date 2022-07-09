package cn.net.insurance.account.cms.connector.mapper;

import cn.net.insurance.account.cms.connector.entity.SysRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 系统角色-权限关系 Mapper 接口
 * </p>
 *
 * @author panxf
 * @since 2021-11-29
 */
@Mapper
@Component
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {


    @Select("select count(1) from sys_role_permission  where permission_id=#{permissionId} and deleted=0")
    long countByPermissionId(@Param("permissionId") String permissionId);

    @Select("select count(1) from sys_role_permission  where role_id=#{roleId} and deleted=0")
    long countByRoleId(@Param("roleId") String roleId);

    /**
     * 根据角色id 删除关联关系
     *
     * @param roleId
     */
    @Update("update sys_role_permission set deleted=1 where role_id=#{roleId} and deleted=0")
    void deleteByRoleId(@Param("roleId") String roleId);


    @Select("select id,role_id,permission_id from sys_role_permission  where permission_id=#{permissionId} and deleted=0")
    List<SysRolePermission> selectByPermissionId(@Param("permissionId") String permissionId);

    @Select("select id,role_id,permission_id from sys_role_permission  where role_id=#{roleId} and deleted=0")
    List<SysRolePermission> selectByRoleId(@Param("roleId") String roleId);

}
