package com.shiro.session;

import java.io.Serializable;

import javax.servlet.ServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

/**
* @Description: 自定义SessionManager  自带的SessionManage会增加redis的压力(一次访问多次读取等问题)
* @author chenhang
* @date 2019年6月13日
*
*/
public class CustomSessionManager extends DefaultWebSessionManager{

	    
	@Override
	protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {	
		Serializable sessionId = getSessionId(sessionKey);
		ServletRequest request = null;
		if (sessionKey instanceof WebSessionKey) {
			request = ((WebSessionKey)sessionKey).getServletRequest();
		}
		if (request != null && sessionId != null) {
			Session session = (Session) request.getAttribute(sessionId.toString());
			if (session != null) {
				return session;
			}
		}
		//request中没有对应的session，就去从redis中取
		Session session = super.retrieveSession(sessionKey);
		if (request != null && sessionId != null) {
			session.setAttribute(sessionId.toString(), session);
		}
		return session;
	}
	
}
