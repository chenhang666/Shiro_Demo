package com.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * @Description: 角色过滤器
 * @author chenhang
 * @date 2019年6月13日
 *
 */
public class RolesFilter extends AuthorizationFilter{

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        Subject subject = getSubject(request, response);
        String[] roles = (String[]) mappedValue;
        if (roles == null || roles.length == 0) {
            return true;
        }
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

}
