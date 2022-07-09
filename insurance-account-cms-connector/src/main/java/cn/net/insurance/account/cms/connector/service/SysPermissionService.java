package cn.net.insurance.account.cms.connector.service;

import cn.net.insurance.account.cms.connector.entity.SysPermission;
import cn.net.insurance.account.common.dto.req.*;
import cn.net.insurance.account.common.dto.resp.PermissionAuthRespDto;
import cn.net.insurance.account.common.dto.resp.PermissionTreeNodeDto;
import cn.net.insurance.core.base.model.RespResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 创建菜单/权限
     *
     * @return 账号id
     */
    RespResult<String> createPermission(PermissionCreateReqDto createReqDto);

    /**
     * 删除菜单/权限
     */
    RespResult<Void> deletePermissionById(String permissionId);


    /**
     * 更新单/权限
     *
     * @param modifyReqDto
     */
    RespResult<Void> modifyPermission(PermissionModifyReqDto modifyReqDto);



    RespResult<List<String>> listAuthCodeByAccountId(String accountId, int btnType);

    /**
     * 左侧菜单
     *
     * @param leftMenuTreeReqDto
     * @return
     */
    RespResult<PermissionTreeNodeDto> leftMenuTree(LeftMenuTreeReqDto leftMenuTreeReqDto);

    /**
     * 角色选择权限，根据parentId返回整个权限树,如果角色不为空则标记选中的权限
     *
     * @param selectPermissionReqDto
     * @return
     */
    RespResult<PermissionTreeNodeDto> selectPermission(SelectPermissionReqDto selectPermissionReqDto);

    /**
     * 权限管理页面，根据条件入参查询树形菜单
     *
     * @param queryReqDto
     * @return
     */
    RespResult<PermissionTreeNodeDto> permissionTreeNode(PermissionTreeNodeReqDto queryReqDto);

    /**
     * 查询权限列表 （没有层级关系） 权限鉴权需要用到url 和 auth
     *
     * @param accountId
     * @return
     */
    RespResult<List<PermissionAuthRespDto>> listAuthByAccountId(String accountId);

}
