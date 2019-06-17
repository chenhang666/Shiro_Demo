package com.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
* @Description: 自定义Realm Test
* @author chenhang
* @date 2019年6月11日
*
*/
public class UserRealmTest {

	@Test
	public void testAuthentication() {
				
		UserRealm userRealm = new UserRealm();
		
		//1、构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(userRealm);
		
		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("lisi", "123456");
		subject.login(token);
		
		//判断是否登录成功	
		if (subject.isAuthenticated()) {System.out.println("登录成功");}else {System.out.println("登录成功");}
		
		subject.checkRoles("admin","none");
		
		subject.checkPermissions("user:add","user:delete");
					
	}
	
}
