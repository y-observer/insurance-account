package cn.net.insurance.account.common.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author : panxf
 * @Date : 2021/12/6 9:47
 */
@Data
public class PhoneForgetReqDto {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String password;
}
