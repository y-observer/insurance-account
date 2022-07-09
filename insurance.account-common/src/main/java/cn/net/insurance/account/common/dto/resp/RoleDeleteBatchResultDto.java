package cn.net.insurance.account.common.dto.resp;

import lombok.Data;

import java.util.List;

/**
 * @Author : panxf
 * @Date : 2021/11/30 20:44
 */
@Data
public class RoleDeleteBatchResultDto {

    private List<RoleDeleteRespDto> listSuccess;
    private List<RoleDeleteRespDto> listFail;

}
