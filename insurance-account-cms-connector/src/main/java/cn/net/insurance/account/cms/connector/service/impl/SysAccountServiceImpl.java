package cn.net.insurance.account.cms.connector.service.impl;

import cn.net.insurance.account.cms.connector.entity.SysAccount;
import cn.net.insurance.account.cms.connector.entity.SysAccountRole;
import cn.net.insurance.account.cms.connector.entity.SysRole;
import cn.net.insurance.account.cms.connector.mapper.SysAccountMapper;
import cn.net.insurance.account.cms.connector.mapper.SysAccountRoleMapper;
import cn.net.insurance.account.cms.connector.mapper.SysRoleMapper;
import cn.net.insurance.account.cms.connector.service.SysAccountService;
import cn.net.insurance.account.common.constant.AccountConstant;
import cn.net.insurance.account.common.constant.RedisKeys;
import cn.net.insurance.account.common.dto.req.AccountCreateReqDto;
import cn.net.insurance.account.common.dto.req.AccountModifyReqDto;
import cn.net.insurance.account.common.dto.req.AccountQueryPageReqDto;
import cn.net.insurance.account.common.dto.req.LoginReqDto;
import cn.net.insurance.account.common.dto.resp.*;
import cn.net.insurance.account.common.enums.CommonDeleteEnum;
import cn.net.insurance.account.common.enums.CommonStateEnum;
import cn.net.insurance.account.common.enums.FirstLoginMarkEnum;
import cn.net.insurance.account.common.enums.SystemTypeEnum;
import cn.net.insurance.core.base.model.ExtraCodeEnum;
import cn.net.insurance.core.base.model.RespResult;
import cn.net.insurance.core.common.model.BasePageResp;
import cn.net.insurance.core.encipher.soft.SM3Util;
import cn.net.insurance.core.mybatis.convert.BasePageRespConvert;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysAccountServiceImpl extends ServiceImpl<SysAccountMapper, SysAccount> implements SysAccountService {

    @Autowired
    private SysAccountMapper sysAccountMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysAccountRoleMapper accountRoleMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${login.token.expire.seconds:1800}")
    private long expireTokenSeconds;

    @Override
    public RespResult<String> createAccount(AccountCreateReqDto createReqDto) throws UnsupportedEncodingException {

        long countByPhoneNumber = sysAccountMapper.countByPhoneNumber(null, createReqDto.getPhoneNumber());
        if (countByPhoneNumber > 0) {
            return RespResult.fail(ExtraCodeEnum.PHONE_HAS_EXISTS);
        }
        long countByUsername = sysAccountMapper.countByUsername(null, createReqDto.getUsername());
        if (countByUsername > 0) {
            return RespResult.fail(ExtraCodeEnum.USERNAME_EXISTS);
        }

        SysAccount sysAccount = new SysAccount();
        String salt = SM3Util.getSalt();
        String sm3PassWord = SM3Util.encrypt(createReqDto.getPassword(), salt);
        sysAccount.setPassword(sm3PassWord);
        sysAccount.setSalt(salt);
        sysAccount.setDeleted(CommonDeleteEnum.NORMAL.getCode());
        sysAccount.setState(createReqDto.getState());
        sysAccount.setName(createReqDto.getName());
        sysAccount.setPhoneNumber(createReqDto.getPhoneNumber());
        sysAccount.setTitle(createReqDto.getTitle());
        sysAccount.setUsername(createReqDto.getUsername());
        sysAccountMapper.insert(sysAccount);

        if (!CollectionUtils.isEmpty(createReqDto.getRoleIds())) {
            createReqDto.getRoleIds().stream().forEach(roleId -> {
                SysAccountRole accountRole = new SysAccountRole();
                accountRole.setAccountId(sysAccount.getId());
                accountRole.setRoleId(roleId);
                accountRoleMapper.insert(accountRole);
            });
        }
        return RespResult.success(sysAccount.getId());
    }


    @Override
    public RespResult<Void> deleteAccountById(String accountId) {
        if (AccountConstant.ADMIN_ACCOUNT_ID.equals(accountId)) {
            return RespResult.fail(ExtraCodeEnum.DEFAULT_DATA_FORBID_DELETE);
        }
        sysAccountMapper.deleteAccountById(accountId);
        accountRoleMapper.deleteByAccountId(accountId);
        return RespResult.success();
    }

    @Override
    public SysAccount queryByUsername(String username) {
        return sysAccountMapper.queryByUsername(username);
    }

    @Override
    public RespResult<LoginRespDto> login(SysAccount account, LoginReqDto loginReqDto) {
        if (FirstLoginMarkEnum.UNLOGIN.getCode().equals(account.getFirstLoginMark())) {
            //预留，做一些首次登录的初始化操作 ...
            account.setFirstLoginMark(FirstLoginMarkEnum.LOGIN.getCode());
            updateById(account);
        }
        log.info("用户登录成功:{}", account.getUsername());
        LoginRespDto loginRespDto = new LoginRespDto();
        loginRespDto.setId(account.getId());
        loginRespDto.setUsername(account.getUsername());
        loginRespDto.setSalt(account.getSalt());
        loginRespDto.setPhoneNumber(account.getPhoneNumber());
        loginRespDto.setName(account.getName());
        return shiroLogin(RespResult.success(loginRespDto),loginReqDto);
    }

    @Override
    public RespResult<Void> loginOut(String id) {
        String token = null;
        Object accountCacheDtoJson = redisTemplate.opsForValue().get(String.format(RedisKeys.OMS_LOGIN_USER_ID, SystemTypeEnum.INSURANCE.getName(), id));
        if (Objects.nonNull(accountCacheDtoJson)) {
            AccountCacheDto accountCacheDto = JacksonUtils.toObj(accountCacheDtoJson.toString(), AccountCacheDto.class);
            token = accountCacheDto.getToken();
            redisTemplate.delete(String.format(RedisKeys.OMS_LOGIN_USER_ID, SystemTypeEnum.INSURANCE.getName(), id));
        }
        if (StringUtils.isNotBlank(token)) {
            redisTemplate.delete(String.format(RedisKeys.OMS_LOGIN_USER_TOKEN, SystemTypeEnum.INSURANCE.getName(), token));
        }
        //退出登录
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return RespResult.success();
    }

    @Override
    public RespResult<Void> modifyAccount(AccountModifyReqDto modifyReqDto) {

        long countByPhoneNumber = sysAccountMapper.countByPhoneNumber(modifyReqDto.getId(), modifyReqDto.getPhoneNumber());
        if (countByPhoneNumber > 0) {
            return RespResult.fail(ExtraCodeEnum.PHONE_HAS_EXISTS);
        }
        SysAccount sysAccount = sysAccountMapper.selectById(modifyReqDto.getId());
        if (Objects.isNull(sysAccount)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        sysAccount.setDeleted(CommonDeleteEnum.NORMAL.getCode());
        sysAccount.setState(modifyReqDto.getState());
        sysAccount.setName(modifyReqDto.getName());
        sysAccount.setPhoneNumber(modifyReqDto.getPhoneNumber());
        sysAccount.setTitle(modifyReqDto.getTitle());
        sysAccountMapper.updateById(sysAccount);
        accountRoleMapper.deleteByAccountId(modifyReqDto.getId());
        List<String> roleIds = modifyReqDto.getRoleIds();
        if (!CollectionUtils.isEmpty(roleIds)) {
            modifyReqDto.getRoleIds().stream().forEach(roleId -> {
                SysAccountRole accountRole = new SysAccountRole();
                accountRole.setAccountId(modifyReqDto.getId());
                accountRole.setRoleId(roleId);
                accountRoleMapper.insert(accountRole);
            });
        }
        return RespResult.success();
    }

    @Override
    public RespResult<Void> modifyState(String id, Integer state) {
        if (AccountConstant.ADMIN_ACCOUNT_ID.equals(id)) {
            return RespResult.fail(ExtraCodeEnum.INVALID_REQUEST.code, "系统默认账号，不允许修改状态");
        }
        CommonStateEnum stateEnum = CommonStateEnum.findByCode(state);
        if (Objects.isNull(stateEnum)) {
            return RespResult.fail(ExtraCodeEnum.ERROR_REQUEST_PARAM);
        }
        sysAccountMapper.modifyState(id, state);
        return RespResult.success();
    }

    @Override
    public RespResult updatePassword(String phoneNumber, String password) {
        long hasCount = sysAccountMapper.countByPhoneNumber(null, phoneNumber);
        if (hasCount == 0) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA.code, "未找到该账号信息");
        }
        SysAccount sysAccount = new SysAccount();
        String salt = SM3Util.getSalt();
        String sm3PassWord = SM3Util.encrypt(password, salt);
        sysAccount.setPassword(sm3PassWord);
        sysAccount.setPhoneNumber(phoneNumber);
        sysAccount.setSalt(salt);
        long updateCount = sysAccountMapper.updatePassword(sysAccount);
        return updateCount == 1 ? RespResult.success() : RespResult.fail(ExtraCodeEnum.DATA_DEAL_ERROR);
    }

    @Override
    public RespResult<SysAccountDto> queryByUsernameToEntity(String username) {
        SysAccount account = sysAccountMapper.queryByUsername(username);
        if (Objects.isNull(account)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        if (Objects.equals(account.getState(), CommonStateEnum.DISENABLE.getCode())) {
            return RespResult.fail(ExtraCodeEnum.USERNAME_DISABLED);
        }
        SysAccountDto sysAccountDto = new SysAccountDto();
        sysAccountDto.setUserId(account.getId());
        sysAccountDto.setState(account.getState());
        sysAccountDto.setName(account.getName());
        sysAccountDto.setSalt(account.getSalt());
        sysAccountDto.setPhoneNumber(account.getPhoneNumber());
        sysAccountDto.setUsername(username);
        return RespResult.success(sysAccountDto);
    }

    @Override
    public RespResult<AccountWithRoleRespDto> queryWithRoleById(String accountId) {
        SysAccount sysAccount = sysAccountMapper.selectById(accountId);
        if (Objects.isNull(sysAccount)) {
            return RespResult.fail(ExtraCodeEnum.NO_DATA);
        }
        AccountWithRoleRespDto accountDto = new AccountWithRoleRespDto();
        BeanUtils.copyProperties(sysAccount, accountDto);
        List<SysRole> roleList = sysRoleMapper.listByAccountId(sysAccount.getId());
        if (!CollectionUtils.isEmpty(roleList)) {
            List<ShortRoleRespDto> list = roleList.stream().map(sysRole -> {
                ShortRoleRespDto roleDto = new ShortRoleRespDto();
                BeanUtils.copyProperties(sysRole, roleDto);
                return roleDto;
            }).collect(Collectors.toList());
            accountDto.setRoleList(list);
        }
        return RespResult.success(accountDto);
    }

    @Override
    public RespResult<BasePageResp<AccountWithRoleRespDto>> listPage(AccountQueryPageReqDto queryReqDto) {
        IPage page = new Page(queryReqDto.getPageIndex(), queryReqDto.getPageSize());
        IPage<SysAccount> iPage = sysAccountMapper.listPage(page, queryReqDto);
        Function function = (Function<SysAccount, AccountWithRoleRespDto>) sysAccount -> {
            AccountWithRoleRespDto accountDto = new AccountWithRoleRespDto();
            BeanUtils.copyProperties(sysAccount, accountDto);

            List<SysRole> roleList = sysRoleMapper.listByAccountId(sysAccount.getId());
            if (!CollectionUtils.isEmpty(roleList)) {
                List<ShortRoleRespDto> list = roleList.stream().map(sysRole -> {
                    ShortRoleRespDto roleDto = new ShortRoleRespDto();
                    BeanUtils.copyProperties(sysRole, roleDto);
                    return roleDto;
                }).collect(Collectors.toList());
                accountDto.setRoleList(list);
            }
            return accountDto;
        };
        return RespResult.success(BasePageRespConvert.generate(iPage, function));
    }

    /**
     * shiro登录
     * @param loginResult
     * @param loginReqDto
     * @return
     */
    private RespResult shiroLogin(RespResult<LoginRespDto> loginResult, LoginReqDto loginReqDto){
        org.apache.shiro.subject.Subject subject;
        if (!loginResult.result()) {
            return loginResult;
        }
        LoginRespDto account = loginResult.getData();
        subject = SecurityUtils.getSubject();
        String sm3PassWord = SM3Util.encrypt(loginReqDto.getPassword(), account.getSalt());
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginReqDto.getUsername(), sm3PassWord);
        subject.login(usernamePasswordToken);
        String token = subject.getSession().getId().toString();
        AccountCacheDto accountCacheDto = new AccountCacheDto();
        BeanUtils.copyProperties(account, accountCacheDto);
        accountCacheDto.setToken(token);
        redisTemplate.opsForValue().set(String.format(RedisKeys.OMS_LOGIN_USER_TOKEN, SystemTypeEnum.INSURANCE.getName(), token), account.getId(), expireTokenSeconds, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(String.format(RedisKeys.OMS_LOGIN_USER_ID, SystemTypeEnum.INSURANCE.getName(), account.getId()), JacksonUtils.toJson(accountCacheDto), expireTokenSeconds, TimeUnit.SECONDS);
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", token);
        account.setSalt(null);
        account.setName(null);
        map.put("account", account);
        return RespResult.success(map);
    }
}
