package cn.net.insurance.account.common.dto.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PermissionCreateReqDto {

    /**
     * 上级菜单
     */
    @NotBlank(message = "父级权限id不能为空")
    private String parentId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 32, message = "菜单名称长度不能大于{max}字符")
    private String name;

    /**
     * 路由地址(访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头)
     */
    @Length(max = 255, message = "路由地址长度不能大于{max}字符")
    private String routeUrl;

    /**
     * 路由参数
     */
    @Length(max = 255, message = "路由参数长度不能大于{max}字符")
    private String routeParam;

    /**
     * 组件地址(访问的组件路径，如：`system/user/index`)
     */
    @Length(max = 255, message = "组件地址长度不能大于{max}字符")
    private String componentUrl;

    /**
     * url访问方式 PermissionVisitType  0:内部访问，1：外部访问
     */
    private Integer visitType;

    /**
     * 菜单类型BtnTypeEnum, 1:目录 2:菜单 3:按钮权限
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer btnType;

    /**
     * 权限字符
     */
    @Length(max = 64, message = "权限字符长度不能大于{max}字符")
    private String auth;

    /**
     * 小图标样式class
     */
    @Length(max = 64, message = "小图标样式class长度不能大于{max}字符")
    private String icon;

    /**
     * 排序
     */
    @NotNull(message = "排序号不能为空")
    private Integer sort;

    /**
     * 菜单状态 CommonStateEnum  0:禁用，1:启用
     */
    private Integer state;

    /**
     * 显示状态 PermissionShowStateEnum 0:不显示，1:显示
     */
    private Integer showState;

}
