package cn.net.insurance.account.common.dto.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/** 创建账号
 * @Author : panxf
 * @Date : 2021/11/29 15:43
 */
@Data
public class AccountCreateReqDto {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(min = 5, max = 15, message = "用户名长度需为{min}-{max}字符")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Length(min = 2, max = 50, message = "姓名长度需为{min}-{max}字符")
    private String name;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Length(max = 11, message = "手机号长度不能大于{max}字符")
    private String phoneNumber;

    /**
     * 职位
     */
    @NotBlank(message = "职位不能为空")
    @Length(max = 20, message = "职位长度不能大于{max}字符")
    private String title;

    /**
     * 账户状态 CommonStateEnum,0:禁用,1:启用
     */
    @NotNull(message = "账户状态不能为空")
    private Integer state;

    /**
     * 角色id List
     */
    private List<String> roleIds;

}
