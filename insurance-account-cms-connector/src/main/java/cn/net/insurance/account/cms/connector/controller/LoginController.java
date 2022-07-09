package cn.net.insurance.account.cms.connector.controller;

import cn.net.insurance.account.cms.connector.entity.SysAccount;
import cn.net.insurance.account.cms.connector.service.SysAccountService;
import cn.net.insurance.account.common.dto.req.LoginReqDto;
import cn.net.insurance.account.common.dto.resp.LoginRespDto;
import cn.net.insurance.account.common.dto.resp.SysAccountDto;
import cn.net.insurance.account.common.enums.CommonStateEnum;
import cn.net.insurance.core.base.model.ExtraCodeEnum;
import cn.net.insurance.core.base.model.RespResult;
import cn.net.insurance.core.encipher.soft.SM3Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/login")
@Slf4j
@RefreshScope
public class LoginController {

    @Autowired
    private SysAccountService accountService;

    /**
     * 账号密码登录
     * @param loginReqDto
     * @return
     */
    @PostMapping("/login")
    public RespResult<LoginRespDto> login(@Valid @RequestBody LoginReqDto loginReqDto) {
        SysAccount account = accountService.queryByUsername(loginReqDto.getUsername());
        if (Objects.isNull(account)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA.code, "账号或者密码不正确");
        }
        if (Objects.equals(account.getState(), CommonStateEnum.DISENABLE.getCode())) {
            return RespResult.fail(ExtraCodeEnum.USERNAME_DISABLED);
        }
        //登录错误次数限制 todo
      /*  boolean result = accountService.loginLimitValidation(account.getId());
        if (!result) {
            return RespResult.fail(ExtraCodeEnum.LOGIN_LIMIT_OVER.code, ExtraCodeEnum.LOGIN_LIMIT_OVER.msg.replace("%s", String.valueOf(expireErrorMinutes)));
        }*/
        String sm3PassWord = SM3Util.encrypt(loginReqDto.getPassword(), account.getSalt());
        if (!account.getPassword().equals(sm3PassWord)) {
            //错误密码登录次数累计 todo
            //accountService.loginErrorCount(account.getId());
            return RespResult.fail(ExtraCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        return accountService.login(account,loginReqDto);
    }


    /**
     * 登出
     *
     * @param id
     * @return
     */
    @GetMapping("/logout")
    public RespResult<Void> logout(@RequestParam("id") String id) {
        return accountService.loginOut(id);
    }
}
