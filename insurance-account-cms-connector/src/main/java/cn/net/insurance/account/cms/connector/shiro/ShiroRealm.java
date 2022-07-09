package cn.net.insurance.account.cms.connector.shiro;

import cn.net.insurance.account.cms.connector.service.SysAccountService;
import cn.net.insurance.account.cms.connector.service.SysPermissionService;
import cn.net.insurance.account.common.dto.resp.SysAccountDto;
import cn.net.insurance.core.base.model.RespResult;
import com.alibaba.nacos.common.utils.JacksonUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description Shiro权限匹配和账号密码匹配
 * @Author yangp
 * @CreateTime 2021/12/18 11:27
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private SysAccountService accountService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SysAccountDto sysAccount = (SysAccountDto) getAvailablePrincipal(principals);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        RespResult<List<String>> listRespResult = permissionService.listAuthCodeByAccountId(sysAccount.getUserId(), 0);
        Set<String> set = new HashSet<>();
        set.addAll(listRespResult.getData());
        info.setStringPermissions(set);
        return info;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        RespResult<SysAccountDto> userInfo = accountService.queryByUsernameToEntity(username);
        SysAccountDto sysAccountDto = userInfo.getData();
        System.out.println("----->>userInfo=" + JacksonUtils.toJson(sysAccountDto));
        if (sysAccountDto == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                //用户名
                sysAccountDto,
                //密码
                password,
                //realm name
                getName()
        );
        return authenticationInfo;
    }
}
