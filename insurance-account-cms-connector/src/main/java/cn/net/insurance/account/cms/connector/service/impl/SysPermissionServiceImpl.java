package cn.net.insurance.account.cms.connector.service.impl;

import cn.net.insurance.account.cms.connector.convert.PermissionSortComparator;
import cn.net.insurance.account.cms.connector.entity.SysAccount;
import cn.net.insurance.account.cms.connector.entity.SysPermission;
import cn.net.insurance.account.cms.connector.entity.SysRolePermission;
import cn.net.insurance.account.cms.connector.mapper.SysPermissionMapper;
import cn.net.insurance.account.cms.connector.mapper.SysRolePermissionMapper;
import cn.net.insurance.account.cms.connector.service.SysAccountService;
import cn.net.insurance.account.cms.connector.service.SysPermissionService;
import cn.net.insurance.account.common.convert.AccountConvert;
import cn.net.insurance.account.common.dto.req.*;
import cn.net.insurance.account.common.dto.resp.PermissionAuthRespDto;
import cn.net.insurance.account.common.dto.resp.PermissionTreeNodeDto;
import cn.net.insurance.account.common.enums.PermissionTypeEnum;
import cn.net.insurance.core.base.model.ExtraCodeEnum;
import cn.net.insurance.core.base.model.RespResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    @Override
    public RespResult<String> createPermission(PermissionCreateReqDto createReqDto) {
        SysPermission parentPermission = permissionMapper.selectById(createReqDto.getParentId());
        if (Objects.isNull(parentPermission)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA.code, "父级菜单不存在");
        }
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(createReqDto, sysPermission);
        permissionMapper.insert(sysPermission);
        return RespResult.success(sysPermission.getId());
    }

    @Override
    public RespResult<Void> deletePermissionById(String permissionId) {

        long count = rolePermissionMapper.countByPermissionId(permissionId);
        if (count > 0) {
            return RespResult.fail(ExtraCodeEnum.BINDING_EXISTS);
        }
        Integer sonCount = permissionMapper.countByParentId(permissionId);
        if (sonCount > 0) {
            return RespResult.fail(ExtraCodeEnum.SON_DATA_EXISTS);
        }
        permissionMapper.deletePermissionById(permissionId);
        return RespResult.success();
    }

    @Override
    public RespResult<Void> modifyPermission(PermissionModifyReqDto modifyReqDto) {
        SysPermission sysPermission = permissionMapper.selectById(modifyReqDto.getId());
        if (Objects.isNull(sysPermission)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA.code, "菜单不存在");
        }
        SysPermission parentPermission = permissionMapper.selectById(modifyReqDto.getParentId());
        if (Objects.isNull(parentPermission)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA.code, "父级菜单不存在");
        }
        BeanUtils.copyProperties(modifyReqDto, sysPermission);
        permissionMapper.updateById(sysPermission);
        return RespResult.success();
    }

    @Override
    public RespResult<List<String>> listAuthCodeByAccountId(String accountId, int btnType) {
        List<String> list = permissionMapper.listAuthCodeByAccountId(accountId, btnType);
        return RespResult.success(list);
    }

    @Override
    public RespResult<PermissionTreeNodeDto> leftMenuTree(LeftMenuTreeReqDto leftMenuTreeReqDto) {
        String accountId = leftMenuTreeReqDto.getAccountId();
        Integer systemType = leftMenuTreeReqDto.getSystemType();
        String rootId = PermissionTypeEnum.findByCode(systemType).getPermissionId();
        SysPermission rootPermission = permissionMapper.selectById(rootId);
        if (rootPermission == null) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        PermissionTreeNodeDto parentNodeDto = new PermissionTreeNodeDto();
        BeanUtils.copyProperties(rootPermission, parentNodeDto);

        //新增保存时，前端只会吧选中的节点的id传到后台，可能父节点没有点击选中 就不会传父节点id,这里默认如果有子节点的权限，就有其父节点的权限（所以需要往上找其父节点）
        //先获取全量的菜单
        List<SysPermission> allPermissionList = permissionMapper.leftMenuList4Admin();
        //从全量菜单中获取到自己有的菜单
        List<SysPermission> finalPermissionList = new ArrayList<>();
        Set<String> permissionIdSet = new HashSet<>();
        if (AccountConvert.isAdmin(accountId)) {
            finalPermissionList = allPermissionList;
        } else {
            List<SysPermission> sysPermissionList = permissionMapper.leftMenuList4Account(accountId);
            if (!CollectionUtils.isEmpty(sysPermissionList)) {
                for (SysPermission sysPermission : sysPermissionList) {
                    //查找父节点id
                    getPermissionIds(permissionIdSet, sysPermission, allPermissionList);
                }
                for (SysPermission sysPermission : allPermissionList) {
                    if (permissionIdSet.contains(sysPermission.getId())) {
                        finalPermissionList.add(sysPermission);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(finalPermissionList)) {
            //整体排序
            Collections.sort(finalPermissionList, new PermissionSortComparator());
            //构建树形结构
            getChildPermission(parentNodeDto, finalPermissionList, null);
            return RespResult.success(parentNodeDto);
        }
        return RespResult.success(parentNodeDto);
    }


    @Override
    public RespResult<PermissionTreeNodeDto> selectPermission(SelectPermissionReqDto selectPermissionReqDto) {
        String roleId = selectPermissionReqDto.getRoleId();
        Integer systemType = selectPermissionReqDto.getSystemType();
        String rootId = PermissionTypeEnum.findByCode(systemType).getPermissionId();
        SysPermission rootPermission = permissionMapper.selectById(rootId);
        if (rootPermission == null) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        PermissionTreeNodeDto parentNodeDto = new PermissionTreeNodeDto();
        BeanUtils.copyProperties(rootPermission, parentNodeDto);
        //选中的菜单IdSet
        Set<String> selectIdSet = null;
        if (roleId != null) {
            List<SysRolePermission> rolePermissionList = rolePermissionMapper.selectByRoleId(roleId);
            selectIdSet = rolePermissionList.stream().map(rolePermi -> rolePermi.getPermissionId()).collect(Collectors.toSet());
        }
        List<SysPermission> sysPermissionList = permissionMapper.listAllPermission();
        if (!CollectionUtils.isEmpty(sysPermissionList)) {
            //整体排序
            Collections.sort(sysPermissionList, new PermissionSortComparator());
            //构建树形结构
            getChildPermission(parentNodeDto, sysPermissionList, selectIdSet);
            return RespResult.success(parentNodeDto);
        }
        return RespResult.success(parentNodeDto);
    }

    @Override
    public RespResult<PermissionTreeNodeDto> permissionTreeNode(PermissionTreeNodeReqDto queryReqDto) {
        Integer systemType = queryReqDto.getSystemType();
        String rootId = queryReqDto.getRootId();
        if (org.apache.commons.lang3.StringUtils.isBlank(rootId)) {
            rootId = PermissionTypeEnum.findByCode(systemType).getPermissionId();
        }
        SysPermission rootPermission = permissionMapper.selectById(rootId);
        if (rootPermission == null) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        PermissionTreeNodeDto parentNodeDto = new PermissionTreeNodeDto();
        BeanUtils.copyProperties(rootPermission, parentNodeDto);
        List<SysPermission> permissionList = permissionMapper.listByParams(queryReqDto);
        Set<String> permissionIdSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            List<SysPermission> allPermissionList = permissionMapper.listAllPermission();
            for (SysPermission sysPermission : permissionList) {
                //查找父节点id
                getPermissionIds(permissionIdSet, sysPermission, allPermissionList);
            }
            List<SysPermission> finalPermissionList = new ArrayList<>();
            for (SysPermission sysPermission : allPermissionList) {
                if (permissionIdSet.contains(sysPermission.getId())) {
                    finalPermissionList.add(sysPermission);
                }
            }
            //整体排序
            if (!CollectionUtils.isEmpty(finalPermissionList)) {
                Collections.sort(finalPermissionList, new PermissionSortComparator());
            }
            //构建树形结构
            getChildPermission(parentNodeDto, finalPermissionList, null);
        }
        return RespResult.success(parentNodeDto);
    }

    @Override
    public RespResult<List<PermissionAuthRespDto>> listAuthByAccountId(String accountId) {
        List<SysPermission> permissionList = permissionMapper.listAuthByAccountId(accountId);
        List<PermissionAuthRespDto> permissionDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            for (SysPermission permission : permissionList) {
                PermissionAuthRespDto permissionAuthDto = new PermissionAuthRespDto();
                BeanUtils.copyProperties(permission, permissionAuthDto);
                permissionDtoList.add(permissionAuthDto);
            }
        }
        return RespResult.success(permissionDtoList);
    }

    /**
     * 找父节点id,存入permissionIdSet
     */
    private void getPermissionIds(Set<String> permissionIdSet,
                                  SysPermission currentPermission,
                                  List<SysPermission> allPermissionList) {

        permissionIdSet.add(currentPermission.getId());
        if (AccountConvert.isRootPermission(currentPermission.getId())) {
            return;
        }
        if (AccountConvert.isRootPermission(currentPermission.getParentId())) {
            return;
        }
        for (SysPermission sysPermission : allPermissionList) {
            if (sysPermission.getId().equals(currentPermission.getParentId())) {
                getPermissionIds(permissionIdSet, sysPermission, allPermissionList);
            }
        }
    }

    private void getChildPermission(PermissionTreeNodeDto parentNodeDto,
                                    List<SysPermission> sysPermissionList,
                                    Set<String> selectIdSet) {
        if (!CollectionUtils.isEmpty(sysPermissionList)) {
            List<PermissionTreeNodeDto> permissionTreeNodeDtoList = new ArrayList<>();
            for (SysPermission sysPermission : sysPermissionList) {
                if (sysPermission.getParentId().equals(parentNodeDto.getId())) {
                    PermissionTreeNodeDto treeNodeDto = new PermissionTreeNodeDto();
                    BeanUtils.copyProperties(sysPermission, treeNodeDto);
                    permissionTreeNodeDtoList.add(treeNodeDto);
                    if (!CollectionUtils.isEmpty(selectIdSet)
                            && selectIdSet.contains(treeNodeDto.getId())) {
                        treeNodeDto.setSelected(true);
                    }
                    getChildPermission(treeNodeDto, sysPermissionList, selectIdSet);
                }
            }
            parentNodeDto.setChildren(permissionTreeNodeDtoList);
        }
    }
}
