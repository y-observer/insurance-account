package cn.net.insurance.account.common.dto.resp;

import lombok.Data;

@Data
public class AccountCacheDto {

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
     * token
     */
    private String token;

}
