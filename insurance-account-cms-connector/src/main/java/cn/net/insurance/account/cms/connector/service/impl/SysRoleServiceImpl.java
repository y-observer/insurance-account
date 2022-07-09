package cn.net.insurance.account.cms.connector.service.impl;

import cn.net.insurance.account.cms.connector.entity.SysRole;
import cn.net.insurance.account.cms.connector.entity.SysRolePermission;
import cn.net.insurance.account.cms.connector.mapper.SysAccountRoleMapper;
import cn.net.insurance.account.cms.connector.mapper.SysRoleMapper;
import cn.net.insurance.account.cms.connector.mapper.SysRolePermissionMapper;
import cn.net.insurance.account.cms.connector.service.SysPermissionService;
import cn.net.insurance.account.cms.connector.service.SysRoleService;
import cn.net.insurance.account.common.constant.AccountConstant;
import cn.net.insurance.account.common.dto.req.RoleCreateReqDto;
import cn.net.insurance.account.common.dto.req.RoleModifyReqDto;
import cn.net.insurance.account.common.dto.req.RoleQueryPageReqDto;
import cn.net.insurance.account.common.dto.req.SelectPermissionReqDto;
import cn.net.insurance.account.common.dto.resp.*;
import cn.net.insurance.account.common.enums.CommonStateEnum;
import cn.net.insurance.core.base.model.ExtraCodeEnum;
import cn.net.insurance.core.base.model.RespResult;
import cn.net.insurance.core.common.model.BasePageResp;
import cn.net.insurance.core.mybatis.convert.BasePageRespConvert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 系统角色 服务实现类
 * </p>
 *
 * @author panxf
 * @since 2021-11-29
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    @Autowired
    private SysAccountRoleMapper accountRoleMapper;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespResult<String> createRole(RoleCreateReqDto createReqDto) {

        long count = roleMapper.countByName(null, createReqDto.getName());
        if (count > 0) {
            return RespResult.fail(ExtraCodeEnum.DATA_EXISTS);
        }
        SysRole role = new SysRole();
        BeanUtils.copyProperties(createReqDto, role);
        roleMapper.insert(role);
        if (!CollectionUtils.isEmpty(createReqDto.getPermissionIds())) {
            createReqDto.getPermissionIds().stream().forEach(permissionId -> {
                SysRolePermission rolePermission = new SysRolePermission();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermissionId(permissionId);
                rolePermissionMapper.insert(rolePermission);
            });
        }
        return RespResult.success(role.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespResult<Void> deleteRoleById(String roleId) {
        if (AccountConstant.ADMIN_ROLE_ID.equals(roleId)) {
            return RespResult.fail(ExtraCodeEnum.DEFAULT_DATA_FORBID_DELETE);
        }
        long count = accountRoleMapper.countByRoleId(roleId);
        if (count > 0) {
            return RespResult.fail(ExtraCodeEnum.BINDING_EXISTS);
        }
        roleMapper.deleteRoleById(roleId);
        rolePermissionMapper.deleteByRoleId(roleId);
        return RespResult.success();
    }

    @Override
    public RespResult<RoleDeleteBatchResultDto> deleteBatchByIds(List<String> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return RespResult.fail(ExtraCodeEnum.ERROR_PARAMS);
        }
        RoleDeleteBatchResultDto respDto = new RoleDeleteBatchResultDto();
        List<RoleDeleteRespDto> successList = new ArrayList<>();
        List<RoleDeleteRespDto> failList = new ArrayList<>();
        roleIdList.stream().forEach(roleId -> {
            SysRole sysRole = roleMapper.selectById(roleId);
            RoleDeleteRespDto roleDeleteRespDto = new RoleDeleteRespDto();
            BeanUtils.copyProperties(sysRole, roleDeleteRespDto);
            long count = accountRoleMapper.countByRoleId(roleId);
            if (count > 0) {
                failList.add(roleDeleteRespDto);
                roleDeleteRespDto.setMsg(ExtraCodeEnum.BINDING_EXISTS.msg);
                return;
            }
            roleMapper.deleteRoleById(roleId);
            rolePermissionMapper.deleteByRoleId(roleId);
            BeanUtils.copyProperties(sysRole, roleDeleteRespDto);
            successList.add(roleDeleteRespDto);
        });
        respDto.setListSuccess(successList);
        respDto.setListFail(failList);
        return RespResult.success(respDto);
    }

    @Override
    public RespResult<Void> modifyState(String id, Integer state) {

        CommonStateEnum stateEnum = CommonStateEnum.findByCode(state);
        if (Objects.isNull(stateEnum)) {
            return RespResult.fail(ExtraCodeEnum.ERROR_REQUEST_PARAM);
        }
        roleMapper.modifyState(id, state);
        return RespResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespResult<Void> modifyRole(RoleModifyReqDto modifyReqDto) {

        long count = roleMapper.countByName(modifyReqDto.getId(), modifyReqDto.getName());
        if (count > 0) {
            return RespResult.fail(ExtraCodeEnum.DATA_EXISTS);
        }
        SysRole role = roleMapper.selectById(modifyReqDto.getId());
        if (Objects.isNull(role)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        BeanUtils.copyProperties(modifyReqDto, role);
        roleMapper.updateById(role);
        rolePermissionMapper.deleteByRoleId(role.getId());
        if (!CollectionUtils.isEmpty(modifyReqDto.getPermissionIds())) {
            modifyReqDto.getPermissionIds().stream().forEach(permissionId -> {
                SysRolePermission rolePermission = new SysRolePermission();
                rolePermission.setRoleId(modifyReqDto.getId());
                rolePermission.setPermissionId(permissionId);
                rolePermissionMapper.insert(rolePermission);
            });
        }
        return RespResult.success();
    }

    @Override
    public RespResult<RoleWithPermissionRespDto> queryWithPermissionById(Integer systemType, String roleId) {
        SysRole sysRole = roleMapper.selectById(roleId);
        if (Objects.isNull(sysRole)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        SelectPermissionReqDto selectPermissionReqDto = new SelectPermissionReqDto();
        selectPermissionReqDto.setSystemType(systemType);
        selectPermissionReqDto.setRoleId(roleId);
        RespResult<PermissionTreeNodeDto> selectPermissionResp = permissionService.selectPermission(selectPermissionReqDto);

        RoleWithPermissionRespDto roleDto = new RoleWithPermissionRespDto();
        BeanUtils.copyProperties(sysRole, roleDto);
        if (selectPermissionResp != null && selectPermissionResp.result()) {
            roleDto.setPermissionTreeNodeDto(selectPermissionResp.getData());
        }
        return RespResult.success(roleDto);
    }

    @Override
    public RespResult<BasePageResp<RoleRespDto>> listPage(RoleQueryPageReqDto queryReqDto) {
        IPage page = new Page(queryReqDto.getPageIndex(), queryReqDto.getPageSize());
        IPage<SysRole> iPage = roleMapper.listPage(page, queryReqDto);
        Function function = (Function<SysRole, RoleRespDto>) sysRole -> {
            RoleRespDto roleDto = new RoleRespDto();
            BeanUtils.copyProperties(sysRole, roleDto);
            return roleDto;
        };

        return RespResult.success(BasePageRespConvert.generate(iPage, function));
    }

    @Override
    public RespResult<List<RoleRespDto>> listAll() {
        List<SysRole> sysRoleList = roleMapper.listAll();
        if (!CollectionUtils.isEmpty(sysRoleList)) {
            List<RoleRespDto> roleDtoList = new ArrayList<>();
            sysRoleList.stream().forEach(role -> {
                RoleRespDto roleDto = new RoleRespDto();
                BeanUtils.copyProperties(role, roleDto);
                roleDtoList.add(roleDto);
            });
            return RespResult.success(roleDtoList);
        }
        return RespResult.success();
    }

    @Override
    public RespResult<List<RoleRespDto>> listByAccountId(String accountId) {
        List<SysRole> sysRoleList = roleMapper.listByAccountId(accountId);
        if (!CollectionUtils.isEmpty(sysRoleList)) {
            List<RoleRespDto> roleDtoList = new ArrayList<>();
            sysRoleList.stream().forEach(role -> {
                RoleRespDto roleDto = new RoleRespDto();
                BeanUtils.copyProperties(role, roleDto);
                roleDtoList.add(roleDto);
            });
            return RespResult.success(roleDtoList);
        }
        return RespResult.success();
    }
}
