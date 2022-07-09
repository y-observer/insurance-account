package cn.net.insurance.account.common.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LeftMenuTreeReqDto {

    /**
     * 系统类型 SystemTypeEnum
     */
    @NotNull(message = "系统类型不能为空")
    private Integer systemType;

    /**
     * 账号id
     */
    @NotBlank(message = "账号id不能为空")
    private String accountId;

}
