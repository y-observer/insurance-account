package cn.net.insurance.account.common.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author : panxf
 * @Date : 2021/12/6 11:32
 */
@Data
public class ModifyStateReqDto {

    /**
     * 用户名
     */
    @NotBlank(message = "id不能为空")
    private String id;

    @NotNull(message = "状态不能为空")
    private Integer state;

}
