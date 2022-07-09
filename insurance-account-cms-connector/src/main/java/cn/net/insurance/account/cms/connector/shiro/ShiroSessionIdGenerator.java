package cn.net.insurance.account.cms.connector.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

/**
 * @Description 自定义SessionId生成器
 * @Author yangp
 * @CreateTime 2021/12/18 11:48
 */
public class ShiroSessionIdGenerator implements SessionIdGenerator {
    /**
     * 实现SessionId生成
     *
     * @Author yangp
     * @CreateTime 2021/12/18 11:54
     */
    @Override
    public Serializable generateId(Session session) {
        Serializable sessionId = new JavaUuidSessionIdGenerator().generateId(session);
        return sessionId;
    }
}
