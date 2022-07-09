package cn.net.insurance.account.common.dto.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author : panxf
 * @Date : 2021/12/1 20:40
 */
@Data
public class SelectPermissionReqDto {

    /**
     * SystemTypeEnum
     */
    @NotNull(message = "系统类型不能为空")
    private Integer systemType;

    /**
     * 角色id
     */
    private String roleId;

}
