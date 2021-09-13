package com.zebone.platform.framework.security.redis;

import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.utils.RedisUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class RedisCache<K, V> implements Cache<K, V> {
 
    
	 static Logger logger = LogManager.getLogger(RedisCache.class.getName());
 
 
    public RedisCache(){
    	
    }

    /**
     * The Redis key prefix for the sessions 
     */
    private String keyPrefix = "shiro_session:";
 
    /**
     * Returns the Redis session keys
     * prefix.
     * @return The prefix
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }
 
    /**
     * Sets the Redis sessions key 
     * prefix.
     * @param keyPrefix The prefix
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
 
 
 
    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = RedisUtils.getRedisPreName()+this.keyPrefix + key;
            return preKey.getBytes();
        } else if(key instanceof PrincipalCollection){
            String preKey = RedisUtils.getRedisPreName() + this.keyPrefix + key.toString();
            return preKey.getBytes();
        }else{
            return (RedisUtils.getRedisPreName() + new String(SerializeUtils.serialize(key))).getBytes();
        }
    }
    
    
    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private String getStringKey(K key) {
        if (key instanceof String) {
            String preKey = RedisUtils.getRedisPreName()+ this.keyPrefix + key;
            return preKey;
        } else if(key instanceof PrincipalCollection){
        	User u = (User)((PrincipalCollection)key).getPrimaryPrincipal();
            String preKey = RedisUtils.getRedisPreName() + this.keyPrefix + u.getLoginName();
            return preKey;
        }else{
            return RedisUtils.getRedisPreName() + (new String(SerializeUtils.serialize(key)));
        }
    }
    
    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private String getStringKey(String key) {
    
            String preKey = RedisUtils.getRedisPreName() + this.keyPrefix + key;
            return preKey;
       
    }
    
    
 
    @Override
    public V get(K key) throws CacheException {
        logger.debug("根据key从Redis中获取对象 key [" + key + "]");
        try {
            if (key == null) {
                return null;
            } else {	
            	byte[] rawValue = (byte[]) RedisUtils.getDefaultRedisTemplate().opsForValue().get(getStringKey(key));
                @SuppressWarnings("unchecked") V value = (V) SerializeUtils.deSerialize(rawValue);
                return value;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
 
    }
    public String getStr(String key) throws CacheException {
        logger.debug("根据key从Redis中获取对象 key [" + key + "]");
        try {
            if (key == null) {
                return null;
            } else {
                return (String) RedisUtils.getDefaultRedisTemplate().opsForValue().get(getStringKey(key));
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
         
    }
 
    @Override
    public V put(K key, V value) throws CacheException {
        logger.debug("根据key从存储 key [" + key + "]");
        try {
        	RedisUtils.getDefaultRedisTemplate().opsForValue().set(getStringKey(key), SerializeUtils.serialize(value));
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
 
    public String putStr(String key, String value) throws CacheException {
        logger.debug("根据key从存储 key [" + key + "]");
        try {
        	RedisUtils.getDefaultRedisTemplate().opsForValue().set(getStringKey(key), value);
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
 
    public String put(String key,String value, int expire) throws CacheException {
        logger.debug("根据key从存储 key [" + key + "]");
        try {
        	
        	RedisUtils.getDefaultRedisTemplate().opsForValue().set(getStringKey(key), value, expire/1000 , TimeUnit.SECONDS);

            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
 
    public String removeString(String key) throws CacheException {
        logger.debug("从redis中删除 key [" + key + "]");
        try {
            String previous =(String) RedisUtils.getDefaultRedisTemplate().opsForValue().get(getStringKey(key));
            RedisUtils.getDefaultRedisTemplate().delete(getStringKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
 
    @Override
    public V remove(K key) throws CacheException {
        logger.debug("从redis中删除 key [" + key + "]");
        try {
        	boolean hasKey = RedisUtils.getDefaultRedisTemplate().hasKey(getStringKey(key));
        	V previous = null;
        	if(hasKey){
        		  previous = get(key);
                  RedisUtils.getDefaultRedisTemplate().delete(getStringKey(key));
        	}
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
 
    @Override
    public void clear() throws CacheException {
        logger.debug("从redis中删除所有元素");
        try {
        	 RedisUtils.getDefaultRedisTemplate().delete( this.keyPrefix);
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
 
    @Override
    public int size() {
        try {
        	return  RedisUtils.getDefaultRedisTemplate().opsForValue().size( this.keyPrefix).intValue();

        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        try {
            Set<byte[]> keys = RedisUtils.getDefaultRedisTemplate().keys( this.keyPrefix + "*");
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            } else {
                Set<K> newKeys = new HashSet<K>();
                for (byte[] key : keys) {
                    newKeys.add((K) key);
                }
                return newKeys;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
 
    @Override
    public Collection<V> values() {
        try {
            Set<byte[]> keys = RedisUtils.getDefaultRedisTemplate().keys( this.keyPrefix + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<V>(keys.size());
                for (byte[] key : keys) {
                    @SuppressWarnings("unchecked") V value = get((K) key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
 
}