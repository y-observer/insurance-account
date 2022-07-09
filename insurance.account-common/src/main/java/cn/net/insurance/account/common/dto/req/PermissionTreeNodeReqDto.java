package cn.net.insurance.account.common.dto.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author : panxf
 * @Date : 2021/11/29 16:21
 */
@Data
public class PermissionTreeNodeReqDto {

    /**
     * 系统类型 SystemTypeEnum
     */
    @NotNull(message = "系统类型不能为空")
    private Integer systemType;

    /**
     * 如果rootId ==null，使用systemType 找对应的系统权限根节点id
     */
    private String rootId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单状态 CommonStateEnum  0:禁用，1:启用
     */
    private Integer state;

}
