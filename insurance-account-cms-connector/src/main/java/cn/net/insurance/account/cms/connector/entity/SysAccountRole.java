package cn.net.insurance.account.cms.connector.entity;

import cn.net.insurance.core.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * sys_account
 *
 * @author
 */
@Data
@TableName(autoResultMap = true, value = "sys_account_role")
public class SysAccountRole extends BaseEntity implements Serializable {


    /**
     * 账号id
     */
    private String accountId;

    /**
     * 角色id
     */
    private String roleId;

}