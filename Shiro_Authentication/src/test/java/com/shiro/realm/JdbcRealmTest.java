package com.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;

/**
* @Description: JdbcRealm Demo
* @author chenhang
* @date 2019年6月11日
*
*/
public class JdbcRealmTest {

	DruidDataSource dataSource = new DruidDataSource();
	
	{
		dataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
	}
	
	@Test
	public void testAuthentication() {
		
		//	JdbcRealm的权限开关默认为false
		JdbcRealm jdbcRealm = new JdbcRealm();
		jdbcRealm.setDataSource(dataSource);
		jdbcRealm.setPermissionsLookupEnabled(true);
		
		/*
		 * 	自定义sql
		 * String sql = "select password from test_user where name = ? ";
		jdbcRealm.setAuthenticationQuery(sql);*/
		
		//	jdbcRealm.setUserRolesQuery(sql);		根据用户名查询角色
		//  jdbcRealm.setPermissionsQuery(sql);			根据角色查询权限
		
		//1、构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(jdbcRealm);
		
		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("chenhang", "123456");
		//UsernamePasswordToken token = new UsernamePasswordToken("Tony", "123456");
		subject.login(token);
		
		//判断是否登录成功	
		if (subject.isAuthenticated()) {System.out.println("登录成功");}else {System.out.println("登录成功");}
		
		subject.checkRole("admin");
		subject.checkRoles("admin","none");
		
		subject.checkPermission("user:add");
		subject.checkPermissions("user:add","user:update");
			
	}
	
}
