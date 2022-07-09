package cn.net.insurance.account.cms.connector.shiro;

import cn.net.insurance.core.base.model.ExtraCodeEnum;
import cn.net.insurance.core.base.model.RespResult;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author yangp
 */
public class CustomAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            RespResult respResult = RespResult.fail(ExtraCodeEnum.INVALID_TOKEN.code, "账号未登录");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JacksonUtils.toJson(respResult));
            out.flush();
            return false;
        }
        return false;
    }
}
