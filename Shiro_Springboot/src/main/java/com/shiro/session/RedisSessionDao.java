package com.shiro.session;

import com.shiro.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
* @Description: 
* @author chenhang
* @date 2019年6月13日
*
*/
public class RedisSessionDao extends AbstractSessionDAO{

	@Autowired
	private JedisUtil jedisUtil;
	
	private final String SHIRO_SESSION_PREFIX = "jack-session";
	
	private byte[] getKey(String key) {
		return (SHIRO_SESSION_PREFIX + key).getBytes();
	}
	    
	@Override
	protected Serializable doCreate(Session session) {		
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		save(session);
		return sessionId;
	}
	
	public void save(Session session) {
		if (session != null && session.getId() != null) {
			byte[] key = getKey(session.getId().toString());
			byte[] value = SerializationUtils.serialize(session);
			jedisUtil.set(key,value);
			jedisUtil.expire(key,3600);
		}
	}
	
	@Override
	public void update(Session session) throws UnknownSessionException {
		save(session);	
	}
	    
	@Override
	public void delete(Session session) {
		if (session != null && session.getId() != null) {
			byte[] key = getKey(session.getId().toString());
			jedisUtil.del(key);
		}
	}
	    
	@Override
	public Collection<Session> getActiveSessions() {
		Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
		Set<Session> sessions = new HashSet<>();
		if (CollectionUtils.isEmpty(keys)) {
			return sessions;
		}
		for (byte[] key : keys) {
			Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
			sessions.add(session);
		}
		return sessions;
	}
	    
	    
	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			return null;
		}
		byte[] key = getKey(sessionId.toString());
		byte[] value = jedisUtil.get(key);
		return (Session) SerializationUtils.deserialize(value);
	}

}
