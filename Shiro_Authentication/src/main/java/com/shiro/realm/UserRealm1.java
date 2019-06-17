package com.shiro.realm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
* @Description: 自定义Realm 加密
* @author chenhang
* @date 2019年6月11日
*
*/
public class UserRealm1 extends AuthorizingRealm{
	
	Map<String,String> userMap = new HashMap<>();
	
	{
		//e10adc3949ba59abbe56e057f20f883e  123456
		//5c51b703b1399a874e12d38a4cf33e46  123456 + tony
		//1b539b60601b934441308049a9526e7d  123456 + lisi
 		userMap.put("chenhang", "5c51b703b1399a874e12d38a4cf33e46");
		userMap.put("zhangsan", "e10adc3949ba59abbe56e057f20f883e");
		userMap.put("lisi", "1b539b60601b934441308049a9526e7d");
		
		//设置realm名字
		this.setName("userRealm");
	}

	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) principals.getPrimaryPrincipal();
		Set<String> roles = getRolesByUserName(userName);
		Set<String> permission = getPermissionByUserName(userName);
		SimpleAuthorizationInfo  authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(roles);
		authorizationInfo.setStringPermissions(permission);
		return authorizationInfo;
	}

	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1、从主体传过来的认证信息中，获取用户名
		String userName = (String) token.getPrincipal();
		
		//2、通过用户名获取凭证
		String password = getPasswordByUserName(userName);
		if (password == null) {return null;}
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("chenhang", password,"userRealm");
		//将盐设置入authenticationInfo中
		authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("tony"));
		return authenticationInfo;
	}

	private String getPasswordByUserName(String userName) {
		return userMap.get("chenhang");
	}
	
	private Set<String> getPermissionByUserName(String userName) {
		Set<String> sets = new HashSet<>();
		sets.add("user:add");
		sets.add("user:delete");
		return sets;
	}

	private Set<String> getRolesByUserName(String userName) {
		Set<String> sets = new HashSet<>();
		sets.add("admin");
		sets.add("none");
		return sets;
	}

	public static void main(String[] args) {
		//Md5Hash md5Hash = new Md5Hash("123456");
		Md5Hash md5Hash = new Md5Hash("123456", "tony");
		System.out.println(md5Hash.toString());
		
	}
	
}
