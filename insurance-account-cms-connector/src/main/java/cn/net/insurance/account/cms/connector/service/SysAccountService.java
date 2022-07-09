package cn.net.insurance.account.cms.connector.service;

import cn.net.insurance.account.cms.connector.entity.SysAccount;
import cn.net.insurance.account.common.dto.req.AccountCreateReqDto;
import cn.net.insurance.account.common.dto.req.AccountModifyReqDto;
import cn.net.insurance.account.common.dto.req.AccountQueryPageReqDto;
import cn.net.insurance.account.common.dto.req.LoginReqDto;
import cn.net.insurance.account.common.dto.resp.AccountWithRoleRespDto;
import cn.net.insurance.account.common.dto.resp.LoginRespDto;
import cn.net.insurance.account.common.dto.resp.SysAccountDto;
import cn.net.insurance.core.base.model.RespResult;
import cn.net.insurance.core.common.model.BasePageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.UnsupportedEncodingException;

public interface SysAccountService extends IService<SysAccount> {

    /**
     * 创建账号信息
     *
     * @return 账号id
     */
    RespResult<String> createAccount(AccountCreateReqDto createReqDto) throws UnsupportedEncodingException;

    /**
     * 删除账号
     */
    RespResult<Void> deleteAccountById(String accountId);

    /**
     * 根据用户名获取账号
     *
     * @param username
     * @return
     */
    SysAccount queryByUsername(String username);

    /**
     * 登录
     * @param sysAccount
     * @return
     */
    RespResult<LoginRespDto> login(SysAccount sysAccount, LoginReqDto loginReqDto);

    /**+
     * 登出
     * @param id
     * @return
     */
    RespResult<Void> loginOut(String id);

    /**
     * 更新账号信息
     */
    RespResult<Void> modifyAccount(AccountModifyReqDto modifyReqDto);


    /**
     * 修改状态
     *
     * @param id
     * @return
     */
    RespResult<Void> modifyState(String id, Integer state);


    RespResult updatePassword(String phoneNumber, String password);


    RespResult<SysAccountDto> queryByUsernameToEntity(String username);

    /**
     * 根据主键id查询账号,同时查拥有的角色
     *
     * @param accountId
     * @return
     */
    RespResult<AccountWithRoleRespDto> queryWithRoleById(String accountId);


    /**
     * 分页查询账号列表
     *
     * @param queryReqDto
     * @return
     */
    RespResult<BasePageResp<AccountWithRoleRespDto>> listPage(AccountQueryPageReqDto queryReqDto);
}
