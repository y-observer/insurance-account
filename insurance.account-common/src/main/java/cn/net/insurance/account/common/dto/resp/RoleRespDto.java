package cn.net.insurance.account.common.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description :
 * @Author : panxf
 * @Date : 2021/11/29 15:02
 */
@Data
public class RoleRespDto {

    /**
     * 标识id
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 状态 CommonStateEnum
     */
    private Integer state;

    /**
     * 角色描述
     */
    private String remark;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
