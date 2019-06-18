package com.shiro.cache;

import com.shiro.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
* @Description: cache实例
* @author chenhang
* @date 2019年6月13日
*
*/
@SuppressWarnings("unchecked")
@Component
public class RedisCache<K, V> implements Cache<K, V>{
	
	@Resource
    private JedisUtil jedisUtil;

	private final String CACHE_PREFIX = "jack-cache";
	
	private byte[] getKey(K k) {
		if (k instanceof String) {
			return (CACHE_PREFIX+k).getBytes();
		}
		return SerializationUtils.serialize(k);
	}
	
	@Override
	public V get(K k) throws CacheException {
        byte[] value = jedisUtil.get(getKey(k));
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
	}
	    
	@Override
	public V put(K k, V v) throws CacheException {
		byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key, value);
        jedisUtil.expire(key, 600);
        return v;
	}
	    
	@Override
	public V remove(K k) throws CacheException {
		byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(key);
        jedisUtil.del(key);
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
	}

	@Override
	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	    
	@Override
	public Set<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
