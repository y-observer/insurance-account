package cn.net.insurance.account.cms.connector.entity;

import cn.net.insurance.core.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * sys_role_permission
 *
 * @author
 */
@Data
@TableName(autoResultMap = true, value = "sys_role_permission")
public class SysRolePermission extends BaseEntity implements Serializable {

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;

}