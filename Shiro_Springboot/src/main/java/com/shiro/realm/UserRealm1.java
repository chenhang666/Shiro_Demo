package com.shiro.realm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.shiro.dao.PermissionMapper;
import com.shiro.dao.RoleMapper;
import com.shiro.service.UserService;

/**
* @Description: 自定义Realm 加密
* @author chenhang
* @date 2019年6月11日
*
*/
public class UserRealm1 extends AuthorizingRealm{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private PermissionMapper permissionMapper;
		
	{		
		//设置realm名字
		this.setName("userRealm");
	}

	private static final Logger  log = LoggerFactory.getLogger(UserRealm1.class);
	
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) principals.getPrimaryPrincipal();		
		List<String> roleList = roleMapper.getRoleByUserName(userName);
		Set<String> roles = new HashSet<>(roleList);
		log.info("roles:--"+roles.toString());
		List<String> permissionList = new ArrayList<>();		
		roleList.forEach(x->{
			List<String> list = permissionMapper.getPermissionByRoleName(x);
			list.forEach(y->{permissionList.add(y);});		
		});
		Set<String> permission = new HashSet<>(permissionList);
		log.info("permission:--"+permission.toString());
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
		String password = userService.getPasswordByUserName(userName);
		if (password == null) {return null;}
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("chenhang", password,"userRealm");
		//将盐设置入authenticationInfo中
		authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("chenhang" + "salt"));
		return authenticationInfo;
	}

	public static void main(String[] args) {
		//Md5Hash md5Hash = new Md5Hash("123456");
		//Md5Hash md5Hash = new Md5Hash("123456", "tony");
		//System.out.println(md5Hash.toString());
		System.out.println(MD5Pwd("chenhang","123456"));	//f437f8da28236cc9557b24e47b5a753b
		
	}
	
	public static String MD5Pwd(String username, String pwd) {
        // 加密算法MD5
        // salt盐 username + salt
        // 迭代次数
        String md5Pwd = new SimpleHash("MD5", pwd,ByteSource.Util.bytes(username + "salt"), 2).toHex();
        return md5Pwd;
    }

}
