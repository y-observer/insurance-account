package cn.net.insurance.account.cms.connector.mapper;

import cn.net.insurance.account.cms.connector.entity.SysAccountRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 系统账号-角色关系 Mapper 接口
 * </p>
 *
 * @author panxf
 * @since 2021-11-29
 */
@Mapper
@Component
public interface SysAccountRoleMapper extends BaseMapper<SysAccountRole> {

    /**
     * 根据账号id 删除关联关系
     *
     * @param accountId
     */
    @Update("update sys_account_role set deleted=1 where account_id=#{accountId} and deleted=0")
    void deleteByAccountId(@Param("accountId") String accountId);

    /**
     * 根据账号id 查询
     *
     * @param accountId
     */
    @Select("select id, account_id, role_id from sys_account_role  where account_id=#{accountId} and deleted=0")
    List<SysAccountRole> selectByAccountId(@Param("accountId") String accountId);

    /**
     * 根据角色id 查询
     *
     * @param roleId
     */
    @Select("select id, account_id, role_id from sys_account_role  where role_id=#{roleId} and deleted=0")
    List<SysAccountRole> selectByRoleId(@Param("roleId") String roleId);


    /**
     * 根据角色id 查询数量
     *
     * @param roleId
     */
    @Select("select count(1) from sys_account_role  where role_id=#{roleId} and deleted=0")
    long countByRoleId(@Param("roleId") String roleId);

}
