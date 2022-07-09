package cn.net.insurance.account.cms.connector.controller;

import cn.net.insurance.account.cms.connector.entity.SysAccount;
import cn.net.insurance.account.cms.connector.entity.SysPermission;
import cn.net.insurance.account.cms.connector.service.SysAccountService;
import cn.net.insurance.account.cms.connector.service.SysPermissionService;
import cn.net.insurance.account.common.convert.AccountConvert;
import cn.net.insurance.account.common.dto.req.*;
import cn.net.insurance.account.common.dto.resp.PermissionAuthRespDto;
import cn.net.insurance.account.common.dto.resp.PermissionRespDto;
import cn.net.insurance.account.common.dto.resp.PermissionTreeNodeDto;
import cn.net.insurance.account.common.enums.PermissionTypeEnum;
import cn.net.insurance.core.base.model.ExtraCodeEnum;
import cn.net.insurance.core.base.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 *系统权限 前端控制器
 */
@RestController
@RequestMapping("/permission")
@Slf4j
@RefreshScope
public class SysPermissionController {

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysAccountService sysAccountService;

    /**
     * 新增菜单
     *
     * @param
     * @return
     */
    @PostMapping("/create")
    public RespResult<String> create(@Valid @RequestBody PermissionCreateReqDto createReqDto) {
        return permissionService.createPermission(createReqDto);
    }

    /**
     * 根据主键id删除权限菜单
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public RespResult<Void> deleteById(@RequestParam("id") String id) {
        PermissionTypeEnum permissionTypeEnum = PermissionTypeEnum.findByPermissionId(id);
        if (Objects.nonNull(permissionTypeEnum)) {
            return RespResult.fail(ExtraCodeEnum.DEFAULT_DATA_FORBID_DELETE);
        }
        return permissionService.deletePermissionById(id);
    }

    /**
     * 修改菜单
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public RespResult<Void> modify(@Valid @RequestBody PermissionModifyReqDto modifyReqDto) {
        if (modifyReqDto.getId().equals(modifyReqDto.getParentId())) {
            return RespResult.fail(ExtraCodeEnum.ERROR_REQUEST_PARAM.code, "父子级id不能相同");
        }
        if (AccountConvert.isSystemPermission(modifyReqDto.getId())
                && !AccountConvert.isRootPermission(modifyReqDto.getParentId())) {
            return RespResult.fail(ExtraCodeEnum.ERROR_REQUEST_PARAM.code, "默认的系统模块父级id不能修改");
        }
        return permissionService.modifyPermission(modifyReqDto);
    }

    /**
     * 根据主键id查询账号
     *
     * @param id
     * @return
     */
    @GetMapping("/querybyid")
    public RespResult<PermissionRespDto> queryById(@RequestParam("id") String id) {

        SysPermission permission = permissionService.getById(id);
        if (Objects.isNull(permission)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        PermissionRespDto permissionDto = new PermissionRespDto();
        BeanUtils.copyProperties(permission, permissionDto);
        return RespResult.success(permissionDto);
    }


    /**
     * 左侧菜单
     *
     * @param leftMenuTreeReqDto
     * @return
     */
    @PostMapping("/leftmenutree")
    public RespResult<PermissionTreeNodeDto> leftMenuTree(@Valid @RequestBody LeftMenuTreeReqDto leftMenuTreeReqDto) {
        PermissionTypeEnum permissionTypeEnum = PermissionTypeEnum.findByCode(leftMenuTreeReqDto.getSystemType());
        if (Objects.isNull(permissionTypeEnum)) {
            return RespResult.fail(ExtraCodeEnum.ERROR_REQUEST_PARAM);
        }
        return permissionService.leftMenuTree(leftMenuTreeReqDto);
    }

    /**
     * 根据账号获取当前权限标识
     *
     * @param account
     * @return
     */
    @GetMapping("/getcurrentauth")
    public RespResult<List<String>> getCurrentAuth(@RequestParam("account") String account, @RequestParam(value = "btnType", defaultValue = "0") int btnType) {
        SysAccount sysAccount = sysAccountService.queryByUsername(account);
        if (Objects.isNull(sysAccount)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        return permissionService.listAuthCodeByAccountId(sysAccount.getId(), btnType);
    }

    /**
     * 角色选择权限，根据systemType返回整个权限树,如果角色不为空则标记选中的权限
     *
     * @return
     */
    @PostMapping("/selectpermission")
    public RespResult<PermissionTreeNodeDto> selectPermission(@Valid @RequestBody SelectPermissionReqDto selectPermissionReqDto) {
        PermissionTypeEnum permissionTypeEnum = PermissionTypeEnum.findByCode(selectPermissionReqDto.getSystemType());
        if (Objects.isNull(permissionTypeEnum)) {
            return RespResult.fail(ExtraCodeEnum.ERROR_REQUEST_PARAM);
        }
        return permissionService.selectPermission(selectPermissionReqDto);
    }


    /**
     * 权限管理页面，根据条件入参查询树形菜单
     *
     * @param queryReqDto
     * @return
     */
    @PostMapping("/permissiontreenode")
    public RespResult<PermissionTreeNodeDto> permissionTreeNode(
            @Valid @RequestBody PermissionTreeNodeReqDto queryReqDto) {
        PermissionTypeEnum permissionTypeEnum = PermissionTypeEnum.findByCode(queryReqDto.getSystemType());
        if (Objects.isNull(permissionTypeEnum)) {
            return RespResult.fail(ExtraCodeEnum.ERROR_REQUEST_PARAM);
        }
        return permissionService.permissionTreeNode(queryReqDto);
    }
    /**
     * 根据条件入参查询权限列表 （没有层级关系）
     * 权限鉴权需要用到url 和 auth
     *
     * @param accountId
     * @return
     */
    @GetMapping("/listauthbyaccountid")
    public RespResult<List<PermissionAuthRespDto>> listAuthByAccountId(@RequestParam("accountId") String accountId) {
        return permissionService.listAuthByAccountId(accountId);
    }

}
