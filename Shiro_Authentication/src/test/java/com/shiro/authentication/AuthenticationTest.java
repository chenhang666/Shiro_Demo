package com.shiro.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
* @Description: realm认证
* @author chenhang
* @date 2019年6月11日
*
*/
public class AuthenticationTest {
	
	//不支持添加权限
	SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
	
	@Before
	public void addUser() {
		//simpleAccountRealm.addAccount("chenhang", "123456");
		simpleAccountRealm.addAccount("chenhang", "123456","admin","none");
	}

	@Test
	public void testAuthentication() {
		
		//1、构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(simpleAccountRealm);
		
		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("chenhang", "123456");
		subject.login(token);
		
		//判断是否登录成功	
		if (subject.isAuthenticated()) {System.out.println("登录成功");}else {System.out.println("登录成功");}
		
		subject.checkRole("admin");
		//subject.checkRoles("admin","none");
		
		//登出
		subject.logout();
			
	}
	
	
}
