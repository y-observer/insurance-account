package cn.net.insurance.account.cms.connector.entity;

import cn.net.insurance.core.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * sys_account
 *
 * @author
 */
@Data
@TableName(autoResultMap = true, value = "sys_account")
public class SysAccount extends BaseEntity implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码加密盐
     */
    private String salt;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 职位
     */
    private String title;

    /**
     * 账户状态 CommonStateEnum,0:禁用,1:启用
     */
    private Integer state;

    /**
     * 首次登录标记,(首次登录后变为1)
     */
    private Integer firstLoginMark;

    @TableField(exist = false)
    private String userId;

}