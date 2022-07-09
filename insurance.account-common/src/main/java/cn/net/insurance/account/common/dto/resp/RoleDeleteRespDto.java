package cn.net.insurance.account.common.dto.resp;

import lombok.Data;

/**
 * @Author : panxf
 * @Date : 2021/11/30 20:52
 */

@Data
public class RoleDeleteRespDto {

    /**
     * 角色id
     */
    private String id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 提示消息
     */
    private String msg;
}
