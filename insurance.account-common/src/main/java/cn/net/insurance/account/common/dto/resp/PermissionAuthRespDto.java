package cn.net.insurance.account.common.dto.resp;

import lombok.Data;

/**
 * @Author : panxf
 * @Date : 2021/11/30 19:07
 */
@Data
public class PermissionAuthRespDto {

    /**
     * 标识id
     */
    private String id;

    /**
     * 路由地址(访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头)
     */
    private String routeUrl;

    private String componentUrl;

    private String routeParam;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 权限字符
     */
    private String auth;

}
