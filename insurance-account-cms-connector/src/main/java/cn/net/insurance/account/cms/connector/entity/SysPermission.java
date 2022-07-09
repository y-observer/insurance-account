package cn.net.insurance.account.cms.connector.entity;

import cn.net.insurance.core.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * sys_permission
 *
 * @author
 */
@Data
@TableName(autoResultMap = true, value = "sys_permission")
public class SysPermission extends BaseEntity implements Serializable {

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
}