package cn.net.insurance.account.common.dto.req;

import cn.net.insurance.core.common.model.BasePageReq;
import lombok.Data;

/**
 * @Description :
 * @Author : panxf
 * @Date : 2021/11/29 15:36
 */
@Data
public class RoleQueryPageReqDto extends BasePageReq {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 状态 CommonStateEnum
     */
    private Integer state;

}
