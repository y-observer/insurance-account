package cn.net.insurance.account.cms.connector.controller;


import cn.net.insurance.account.cms.connector.entity.SysRole;
import cn.net.insurance.account.cms.connector.service.SysRoleService;
import cn.net.insurance.account.common.dto.req.ModifyStateReqDto;
import cn.net.insurance.account.common.dto.req.RoleCreateReqDto;
import cn.net.insurance.account.common.dto.req.RoleModifyReqDto;
import cn.net.insurance.account.common.dto.req.RoleQueryPageReqDto;
import cn.net.insurance.account.common.dto.resp.RoleDeleteBatchResultDto;
import cn.net.insurance.account.common.dto.resp.RoleRespDto;
import cn.net.insurance.account.common.dto.resp.RoleWithPermissionRespDto;
import cn.net.insurance.account.common.enums.PermissionTypeEnum;
import cn.net.insurance.account.common.enums.SystemTypeEnum;
import cn.net.insurance.core.base.model.ExtraCodeEnum;
import cn.net.insurance.core.base.model.RespResult;
import cn.net.insurance.core.common.model.BasePageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 系统角色 前端控制器
 * </p>
 *
 * @author panxf
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/role")
@Slf4j
@RefreshScope
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

    /**
     * 新增角色
     *
     * @param
     * @return
     */
    @PostMapping("/create")
    public RespResult<String> create(@Valid @RequestBody RoleCreateReqDto createReqDto) {
        return roleService.createRole(createReqDto);
    }

    /**
     * 根据主键id删除角色
     *
     * @param
     * @return
     */
    @GetMapping("/delete")
    public RespResult<Void> deleteById(@RequestParam("id") String id) {
        return roleService.deleteRoleById(id);
    }

    /**
     * 批量删除角色
     *
     * @param
     * @return
     */
    @PostMapping("/deletebatch")
    public RespResult<RoleDeleteBatchResultDto> deleteBatchByIds(@RequestBody List<String> idList) {
        return roleService.deleteBatchByIds(idList);
    }

    /**
     * 修改账号状态 启用、停用账号
     *
     * @param stateReqDto
     * @return
     */
    @PostMapping("/state")
    public RespResult<Void> state(@Valid @RequestBody ModifyStateReqDto stateReqDto) {
        return roleService.modifyState(stateReqDto.getId(), stateReqDto.getState());
    }

    /**
     * 修改角色
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public RespResult<Void> modify(@Valid @RequestBody RoleModifyReqDto modifyReqDto) {
        return roleService.modifyRole(modifyReqDto);
    }

    /**
     * 根据主键id查询角色
     *
     * @param id
     * @return
     */
    @GetMapping("/querybyid")
    public RespResult<RoleRespDto> queryById(@RequestParam("id") String id) {
        SysRole sysRole = roleService.getById(id);
        if (Objects.isNull(sysRole)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        RoleRespDto roleDto = new RoleRespDto();
        BeanUtils.copyProperties(sysRole, roleDto);
        return RespResult.success(roleDto);
    }


    /**
     * 根据主键id查询角色,同时获取整个权限树，并标记已选择的权限
     *
     * @param id
     * @return
     */
    @GetMapping("/querywithpermissionbyid")
    public RespResult<RoleWithPermissionRespDto> queryWithPermissionById(@RequestParam("systemType") Integer systemType,
                                                                         @RequestParam("id") String id) {

        PermissionTypeEnum permissionTypeEnum = PermissionTypeEnum.findByCode(systemType);
        if (Objects.isNull(permissionTypeEnum)) {
            return RespResult.fail(ExtraCodeEnum.ERROR_REQUEST_PARAM);
        }
        return roleService.queryWithPermissionById(systemType, id);
    }

    /**
     * 查询全部角色
     *
     * @return
     */
    @GetMapping("/listall")
    public RespResult<List<RoleRespDto>> listAll() {
        return roleService.listAll();
    }

    /**
     * 分页查询角色
     *
     * @return
     */
    @PostMapping("/listpage")
    public RespResult<BasePageResp<RoleRespDto>> listPage(@Valid @RequestBody RoleQueryPageReqDto queryReqDto) {
        return roleService.listPage(queryReqDto);
    }

    /**
     * 根据accountId获取所拥有的角色
     *
     * @return
     */
    @GetMapping("/listbyaccountid")
    public RespResult<List<RoleRespDto>> listByAccountId(@RequestParam("accountId") String accountId) {
        return roleService.listByAccountId(accountId);
    }

}

