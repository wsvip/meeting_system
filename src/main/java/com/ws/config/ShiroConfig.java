package com.ws.config;

import com.ws.common.shiro.filter.PlatformAuthenticationFilter;
import com.ws.common.shiro.realm.PlatformAuthorizingRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro 配置类
 * @author WS-
 */
@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //自定义拦截器
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<>();
        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        //添加自定义的过滤器Filter到shiro拦截中
        filterMap.put("/**",new PlatformAuthenticationFilter());
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/statics/**", "anon");
        filterChainDefinitionMap.put("/register/**", "anon");
        filterChainDefinitionMap.put("/login/**", "anon");
        //filterChainDefinitionMap.put("/user/**", "authc");
        //filterChainDefinitionMap.put("/doLogin", "anon");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login/index");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        //将自定义拦截器和拦截器都放入shiro中
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.setFilters(filterMap);
        return shiroFilterFactoryBean;
    }
    /**
     * @Author:  WS-
     * @return:
     * @Date:    2019/4/22  23:04
     * @Description: realm
     */
    @Bean
    public PlatformAuthorizingRealm platformAuthorizingRealm(HashedCredentialsMatcher hashedCredentialsMatcher){
        PlatformAuthorizingRealm platformAuthorizingRealm = new PlatformAuthorizingRealm();
        //设置密码校验器
        platformAuthorizingRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return platformAuthorizingRealm;
    }
    /**
     * @Author:  WS-
     * @return:
     * @Date:    2019/4/22  23:02
     * @Description: 密码校验管理器
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //密码校验方式，有SHA-256和MD5
        hashedCredentialsMatcher.setHashAlgorithmName("SHA-256");
        //密码散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        //设置为Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(false);
        return hashedCredentialsMatcher;
    }
    @Bean
    public SecurityManager securityManager(PlatformAuthorizingRealm realm, Authorizer authorizer){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionManager(new DefaultWebSessionManager());
        defaultWebSecurityManager.setAuthorizer(authorizer);
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
