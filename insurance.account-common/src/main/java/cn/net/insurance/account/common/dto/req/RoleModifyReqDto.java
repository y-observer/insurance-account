package cn.net.insurance.account.common.dto.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description :
 * @Author : panxf
 * @Date : 2021/11/29 15:36
 */
@Data
public class RoleModifyReqDto {

    /**
     * 标识id
     */
    @NotBlank(message = "标识id不能为空")
    private String id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Length(max = 32, message = "角色名称长度不能大于{max}字符")
    private String name;

    /**
     * 状态 CommonStateEnum
     */
    @NotNull(message = "角色状态不能为空")
    private Integer state;

    /**
     * 角色描述
     */
    @Length(max = 255, message = "角色描述长度不能大于{max}字符")
    private String remark;

    /**
     * 权限id List
     */
    private List<String> permissionIds;
}
