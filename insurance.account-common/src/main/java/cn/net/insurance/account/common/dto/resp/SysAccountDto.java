package cn.net.insurance.account.common.dto.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysAccountDto implements Serializable {

    private static final long serialVersionUID = 482901721803426337L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phoneNumber;


    /**
     * 账户状态 CommonStateEnum,0:禁用,1:启用
     */
    private Integer state;


    private String userId;
}
