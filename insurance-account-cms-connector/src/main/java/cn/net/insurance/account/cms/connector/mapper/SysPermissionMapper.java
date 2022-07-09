package cn.net.insurance.account.cms.connector.mapper;

import cn.net.insurance.account.cms.connector.entity.SysPermission;
import cn.net.insurance.account.common.dto.req.PermissionTreeNodeReqDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    @Update("update sys_permission set deleted=1 where id=#{permissionId} and deleted=0")
    Integer deletePermissionById(@Param("permissionId") String permissionId);

    @Select("select count(1) from sys_permission where parent_id=#{parentId} and deleted=0")
    Integer countByParentId(@Param("parentId") String parentId);

    List<String> listAuthCodeByAccountId(@Param("accountId") String accountId, @Param("btnType") int btnType);

    List<SysPermission> leftMenuList4Admin();

    List<SysPermission> leftMenuList4Account(@Param("accountId") String accountId);

    List<SysPermission> listByParams(@Param("queryReqDto") PermissionTreeNodeReqDto queryReqDto);

    List<SysPermission> listAllPermission();

    List<SysPermission> listAuthByAccountId(@Param("accountId") String accountId);

}
