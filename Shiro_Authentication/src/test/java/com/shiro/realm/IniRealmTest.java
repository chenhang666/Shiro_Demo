package com.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
* @Description: IniRealm Demo
* @author chenhang
* @date 2019年6月11日
*
*/
public class IniRealmTest {
	
	@Test
	public void testAuthentication() {
		
		IniRealm iniRealm = new IniRealm("classpath:user.ini");
		
		//1、构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(iniRealm);
		
		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("chenhang", "123456");
		subject.login(token);
		
		//判断是否登录成功	
		if (subject.isAuthenticated()) {System.out.println("登录成功");}else {System.out.println("登录成功");}
		
		subject.checkRole("admin");
		
		subject.checkPermission("user:delete");
			
	}
	
}
