package net.jupic.spring.cache.mybatis.handler;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.ibatis.type.BaseTypeHandler;

/**
 * @author Chang jung pil
 *
 */
public abstract class CacheBasedTypeHandler<T> extends BaseTypeHandler<T> {

	protected String cacheManagerName;
	
	protected String cacheName;
	
	protected CacheManager cacheManager;
	
	public CacheBasedTypeHandler(String cacheName) {
		this(null, cacheName);
	}
	
	public CacheBasedTypeHandler(String cacheManagerName, String cacheName) {
		this.cacheManagerName = cacheManagerName;
		this.cacheName = cacheName;
		
		if (cacheManagerName != null) {
			cacheManager = CacheManager.getCacheManager(cacheManagerName);
		} else {
			cacheManager = CacheManager.getInstance();
		}
	}
	
	public void setCacheManagerName(String cacheManagerName) {
		this.cacheManagerName = cacheManagerName;
		this.cacheManager = CacheManager.getCacheManager(cacheManagerName);
	}
	
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	
	public CacheManager getCacheManager() {
		return cacheManager; 
	}
	
	@SuppressWarnings("unchecked")
	public T getItem(Serializable key) {
		Cache cache = cacheManager.getCache(cacheName);
		try {
			return (T) cache.get(key).getObjectValue();
		} catch (NullPointerException ne) {
			return null;
		}
	}
}
