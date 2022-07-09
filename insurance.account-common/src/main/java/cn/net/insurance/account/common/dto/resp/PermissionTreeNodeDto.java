package cn.net.insurance.account.common.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class PermissionTreeNodeDto {

    /**
     * 标识id
     */
    private String id;

    /**
     * 上级菜单
     */
    private String parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由地址(访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头)
     */
    private String routeUrl;

    /**
     * 路由参数
     */
    private String routeParam;

    /**
     * 组件地址(访问的组件路径，如：`system/user/index`)
     */
    private String componentUrl;

    /**
     * url访问方式 PermissionVisitType  0:内部访问，1：外部访问
     */
    private Integer visitType;

    /**
     * 菜单类型BtnTypeEnum, 1:主模块 2:模块菜单 3:按钮权限
     */
    private Integer btnType;

    /**
     * 权限字符
     */
    private String auth;

    /**
     * 小图标样式class
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单状态 CommonStateEnum  0:禁用，1:启用
     */
    private Integer state;

    /**
     * 显示状态 PermissionShowStateEnum 0:不显示，1:显示
     */
    private Integer showState;

    /**
     * 是否选中
     */
    private boolean selected;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 子级权限列表 (前端要求不返回 null)
     */
    private List<PermissionTreeNodeDto> children = new ArrayList<PermissionTreeNodeDto>();

}
