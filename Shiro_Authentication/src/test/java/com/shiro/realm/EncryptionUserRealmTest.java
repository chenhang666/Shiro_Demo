package com.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
* @Description: 自定义Realm 加密 demo
* @author chenhang
* @date 2019年6月12日
*
*/
public class EncryptionUserRealmTest {

	@Test
	public void testAuthentication() {
				
		UserRealm1 userRealm = new UserRealm1();
		
		//1、构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(userRealm);
		
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName("md5");
		matcher.setHashIterations(1);
		userRealm.setCredentialsMatcher(matcher);
		
		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("chenhang", "123456");
		subject.login(token);
		
		//判断是否登录成功	
		if (subject.isAuthenticated()) {System.out.println("登录成功");}else {System.out.println("登录成功");}
		
		subject.checkRoles("admin","none");
		
		subject.checkPermissions("user:add","user:delete");
					
	}
	
}
