package cn.net.insurance.account.common.dto.req;

import cn.net.insurance.core.common.model.BasePageReq;
import lombok.Data;

/**
 * @Author : panxf
 * @Date : 2021/11/29 15:59
 */
@Data
public class AccountQueryPageReqDto extends BasePageReq {


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
     * 账户状态 CommonStateEnum,0:禁用,1:启用
     */
    private Integer state;


}
