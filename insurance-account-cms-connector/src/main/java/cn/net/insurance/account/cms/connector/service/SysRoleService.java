package cn.net.insurance.account.cms.connector.service;

import cn.net.insurance.account.cms.connector.entity.SysRole;
import cn.net.insurance.account.common.dto.req.RoleCreateReqDto;
import cn.net.insurance.account.common.dto.req.RoleModifyReqDto;
import cn.net.insurance.account.common.dto.req.RoleQueryPageReqDto;
import cn.net.insurance.account.common.dto.resp.RoleDeleteBatchResultDto;
import cn.net.insurance.account.common.dto.resp.RoleRespDto;
import cn.net.insurance.account.common.dto.resp.RoleWithPermissionRespDto;
import cn.net.insurance.core.base.model.RespResult;
import cn.net.insurance.core.common.model.BasePageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统角色 服务类
 * </p>
 *
 * @author panxf
 * @since 2021-11-29
 */
public interface SysRoleService extends IService<SysRole> {


    /**
     * 创建角色信息
     *
     * @return 角色id
     */
    RespResult<String> createRole(RoleCreateReqDto createReqDto);

    /**
     * 删除角色
     */
    RespResult<Void> deleteRoleById(String roleId);


    /**
     * 批量删除角色
     */
    RespResult<RoleDeleteBatchResultDto> deleteBatchByIds(List<String> roleIdList);


    /**
     * 修改状态
     *
     * @param id
     * @return
     */
    RespResult<Void> modifyState(String id, Integer state);

    /**
     * 更新角色信息
     *
     * @param modifyReqDto
     */
    RespResult<Void> modifyRole(RoleModifyReqDto modifyReqDto);

    /**
     * 根据主键id查询角色,同时查所有权限，拥有的权限
     *
     * @param systemType
     * @param roleId
     * @return
     */
    RespResult<RoleWithPermissionRespDto> queryWithPermissionById(Integer systemType, String roleId);

    /**
     * 分页查询账号列表
     *
     * @param queryReqDto
     * @return
     */
    RespResult<BasePageResp<RoleRespDto>> listPage(RoleQueryPageReqDto queryReqDto);

    RespResult<List<RoleRespDto>> listAll();

    RespResult<List<RoleRespDto>> listByAccountId(String accountId);


}
