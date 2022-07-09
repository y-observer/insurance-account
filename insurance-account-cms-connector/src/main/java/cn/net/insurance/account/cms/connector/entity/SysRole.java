package cn.net.insurance.account.cms.connector.entity;

import cn.net.insurance.core.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * sys_account
 * @author 
 */
@Data
@TableName(autoResultMap = true, value = "sys_role")
public class SysRole extends BaseEntity implements Serializable {


    /**
     * 角色名称
     */
    private String name;

    /**
     * 状态 CommonStateEnum
     */
    private Integer state;

    /**
     * 角色描述
     */
    private String remark;

}