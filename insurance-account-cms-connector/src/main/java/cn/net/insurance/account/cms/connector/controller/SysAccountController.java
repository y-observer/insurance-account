package cn.net.insurance.account.cms.connector.controller;

import cn.net.insurance.account.cms.connector.service.SysAccountService;
import cn.net.insurance.account.common.dto.req.*;
import cn.net.insurance.account.common.dto.resp.AccountWithRoleRespDto;
import cn.net.insurance.account.common.dto.resp.LoginRespDto;
import cn.net.insurance.core.base.model.ExtraCodeEnum;
import cn.net.insurance.core.base.model.RespResult;
import cn.net.insurance.core.base.utils.CommonUtils;
import cn.net.insurance.core.common.model.BasePageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * 系统登录账号 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/account")
@Slf4j
@RefreshScope
public class SysAccountController {

    @Autowired
    private SysAccountService accountService;

    /**
     * 新增账号
     *
     * @param createReqDto
     * @return
     */
    @PostMapping("/create")
    public RespResult<String> create(@Valid @RequestBody AccountCreateReqDto createReqDto)
            throws UnsupportedEncodingException {
        if(!CommonUtils.isAlphaNumeric(createReqDto.getUsername())){
            return RespResult.fail(ExtraCodeEnum.ERROR_PARAMS.code,"用户名格式不正确");
        }
        if(!CommonUtils.isMobileNumber(createReqDto.getPhoneNumber())){
            return RespResult.fail(ExtraCodeEnum.ERROR_PARAMS.code,"手机号格式不正确");
        }
        return accountService.createAccount(createReqDto);
    }

    /**
     * 删除账号
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public RespResult<Void> deleteById(@RequestParam("id") String id) {
        return accountService.deleteAccountById(id);
    }

    /**
     * 修改账号状态 启用、停用账号
     *
     * @param stateReqDto
     * @return
     */
    @PostMapping("/state")
    public RespResult<Void> state(@Valid @RequestBody ModifyStateReqDto stateReqDto) {
        return accountService.modifyState(stateReqDto.getId(), stateReqDto.getState());
    }

    /**
     * 修改账号
     *
     * @param modifyReqDto
     * @return
     */
    @PostMapping("/modify")
    public RespResult<Void> modify(@Valid @RequestBody AccountModifyReqDto modifyReqDto) {
        return accountService.modifyAccount(modifyReqDto);
    }


    /**
     * 设置密码(忘记密码,没有原密码)
     *
     * @param forgetReqDto
     * @return
     */
    @PostMapping("/setPassword")
    public RespResult<LoginRespDto> forgetPassword(@Valid @RequestBody PhoneForgetReqDto forgetReqDto) {
        return accountService.updatePassword(forgetReqDto.getPhoneNumber(), forgetReqDto.getPassword());
    }

    /**
     * 根据主键id查询账号,同时查拥有的角色
     *
     * @param id
     * @return
     */
    @GetMapping("/querywithrolebyid")
    public RespResult<AccountWithRoleRespDto> queryWithRoleById(@RequestParam("id") String id) {
        return accountService.queryWithRoleById(id);
    }



    /**
     * 分页查询账号
     *
     * @param queryReqDto
     * @return
     */
    @PostMapping("/listpage")
    public RespResult<BasePageResp<AccountWithRoleRespDto>> listPage(
            @Valid @RequestBody AccountQueryPageReqDto queryReqDto) {
        return accountService.listPage(queryReqDto);
    }
}
