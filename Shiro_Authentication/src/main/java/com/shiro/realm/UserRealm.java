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
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @Description: 自定义Realm
 * @author chenhang
 * @date 2019年6月11日
 *
 */
public class UserRealm extends AuthorizingRealm{

    Map<String,String> userMap = new HashMap<>();

    {
        userMap.put("chenhang", "123456");
        userMap.put("zhangsan", "123456");
        userMap.put("lisi", "123456");

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

}
