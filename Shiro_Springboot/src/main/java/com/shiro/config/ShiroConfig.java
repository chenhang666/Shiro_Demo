package com.shiro.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shiro.cache.RedisCacheManager;
import com.shiro.filter.RolesFilter;
import com.shiro.realm.UserRealm1;
import com.shiro.session.CustomSessionManager;
import com.shiro.session.RedisSessionDao;


/**
* @Description: 配置类
* @author chenhang
* @date 2019年6月12日
*
*/
@Configuration
public class ShiroConfig {
	
	@Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");
        //shiroFilterFactoryBean.setUnauthorizedUrl("");	没有权限跳转这个路径
        
        // 自定义filter	还有其他自定义filter
        Map<String, Filter> map = new HashMap<>();
        map.put("roleFilter", rolesFilter());
        shiroFilterFactoryBean.setFilters(map);
        
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/front/**", "anon");
        filterChainDefinitionMap.put("/api/**", "anon");
        filterChainDefinitionMap.put("/login1/**", "anon");
        filterChainDefinitionMap.put("/testRole", "roleFilter[admin,super]");	//拥有其中一个角色即可访问
        
        filterChainDefinitionMap.put("/admin/**", "authc");
        filterChainDefinitionMap.put("/user/**", "authc");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(userRealm());
        //session管理	
        //defaultSecurityManager.setSessionManager(defaultSessionManager()); 
        defaultSecurityManager.setSessionManager(customSessionManager()); 
        defaultSecurityManager.setCacheManager(redisCacheManager());
        return defaultSecurityManager;
    }

   /* @Bean
    public UserRealm userRealm() {
    	UserRealm userRealm = new UserRealm();
    	userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
    	userRealm.setCachingEnabled(false);
        return userRealm;
    }*/
    
    @Bean
    public UserRealm1 userRealm() {
    	UserRealm1 userRealm = new UserRealm1();
    	userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
    	userRealm.setCachingEnabled(false);
        return userRealm;
    }
	
    @Bean(name = "credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        // storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }
    
	/**
     * *
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * *
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * * @return  自动创建代理
     */
    /*@Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }*/
    
    //管理shiro bean生命周期
	@Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
	
	/*AuthorizationAttributeSourceAdvisor实现了pointcut，pointcut中一共两个接口方法，一个是getClassFilter，一个是getMethodMatcher
	 *  getClassFilter会匹配所有类！
	 *  getMethodMatcher匹配所有加了认证注解的方法！
	 * */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public RolesFilter rolesFilter() {
    	return new RolesFilter();
    }
    
    //sesion管理
    @Bean 
    public DefaultSessionManager defaultSessionManager() {
    	DefaultSessionManager defaultSessionManager = new DefaultSessionManager();
    	defaultSessionManager.setSessionDAO(redisSessionDao());
    	return defaultSessionManager;
    }
    
    @Bean
    public CustomSessionManager customSessionManager() {
    	CustomSessionManager customSessionManager = new CustomSessionManager();
    	customSessionManager.setSessionDAO(redisSessionDao());
    	return customSessionManager;
    }

    @Bean
    public RedisSessionDao redisSessionDao() {
    	return new RedisSessionDao();
    } 
    
    //缓存管理
    @Bean
    public RedisCacheManager redisCacheManager() {
    	return new RedisCacheManager();
    }
    
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
    	CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
    	cookieRememberMeManager.setCookie(simpleCookie());
    	return cookieRememberMeManager;
    }
    
    @Bean
    public SimpleCookie simpleCookie() {
    	SimpleCookie simpleCookie = new SimpleCookie();
    	simpleCookie.setMaxAge(3600);  			//单位秒
    	simpleCookie.setName("rememberMe");
    	return simpleCookie;
    }
}
