package net.jupic.spring.cache.service;

import static org.springframework.util.Assert.notNull;

import java.io.Serializable;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Chang jung pil
 *
 */
public abstract class GenericCacheService<T> implements InitializingBean {

	private CacheManager cacheManager;
	
	private String cacheName;

	public GenericCacheService(String cacheName) {
		this.cacheName = cacheName;
	}
	
	@Autowired(required=false)
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	public String getCacheName() {
		return this.cacheName;
	}

	public void add(Serializable key, T value) {
		cacheManager.getCache(cacheName).put(new Element(key, value));
	}
	
	@SuppressWarnings("unchecked")
	public T get(Serializable key) {
		return (T)cacheManager.getCache(cacheName).get(key).getObjectValue();
	}
	
	public void remove(Serializable key) {
		cacheManager.getCache(cacheName).remove(key);
	}
	
	public void clearCache() {
		cacheManager.getCache(cacheName).removeAll();
	}
	
	/**
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		notNull(this.cacheManager, "Property 'cacheManager' is required");
		notNull(this.cacheName, "Property 'cacheName' is required");
		
		initCache();
	}
	
	protected abstract void initCache();
}