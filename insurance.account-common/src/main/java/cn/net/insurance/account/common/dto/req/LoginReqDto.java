package cn.net.insurance.account.common.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginReqDto {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
