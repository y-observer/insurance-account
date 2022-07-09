package cn.net.insurance.account.common.dto.resp;

import lombok.Data;

@Data
public class LoginRespDto {
    /**
     * 标识id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phoneNumber;


    /**
     * 手机号
     */
    private String salt;
}
