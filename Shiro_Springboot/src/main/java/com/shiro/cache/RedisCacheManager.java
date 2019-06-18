package com.shiro.cache;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
* @Description: RedisCacheManager shiro缓存
* @author chenhang
* @date 2019年6月13日
*
*/
public class RedisCacheManager implements CacheManager{

	@Resource
    private RedisCache redisCache;
	
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		// TODO Auto-generated method stub
		return redisCache;
	}

}
