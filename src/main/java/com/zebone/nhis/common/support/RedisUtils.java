package com.zebone.nhis.common.support;

import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * redis缓存操作类，目前缓存存储基于Redis中hash类型  ，如有其他类型（List，Set等）需要，可再扩展
 * redis中name按照目录结构存储  
 *    例如 交易号 属于系统管理 ，名称定义 sys:trancode  
 * 
 * @author 凌军
 *
 */
public class RedisUtils {
	
	private static String redisPreName = null;
	
	private static StringRedisTemplate redisTemplate = null;
	
    public static StringRedisTemplate getRedisTemplate(){
    	
    	if(redisTemplate == null){
    		redisTemplate = ServiceLocator.getInstance().getBean("stringRedisTemplate", StringRedisTemplate.class);
    	}
    	return redisTemplate;
    }
	
   public static String getRedisPreName(){
    	
    	if(redisPreName == null){
    		redisPreName = ApplicationUtils.getPropertyValue("redis.pre.name", "his")+":";
    	}
    	return redisPreName;
    }
    
    
	/**
	 * 获取缓存对象
	 * @param name
	 * @param key
	 * @return
	 */
    public static Object getCacheHashObj(String name,String key){
    	
    	return getRedisTemplate().opsForHash().get(getRedisPreName()+name, key);
      }

    /**
     *  获取缓存对象
     * @param name
     * @param key
     * @param c
     * @return
     */
    public static <T> T getCacheHashObj(String name,String key , Class<T> c){
    	
    	Object ro = getCacheHashObj(name,key);
    	
    	if(ro != null){
    		return JsonUtil.readValue(ro.toString(), c);
    	}

    	return null;
    }
    
    
    /**
     * 保存缓存对象(redis 内部以map类型存储)
     * @param name
     * @param c
     * @return
     */
    public static void setCacheHashObj(String name ,String key, Object o){
    	
    	 if( o == null) return;
    	 getRedisTemplate().opsForHash().put(getRedisPreName()+name, key, JsonUtil.writeValueAsString(o));
    }
    
    
    /**
     * 获取缓存对象
     * @param name
     * @param c
     * @param 秒
     * @return
     */
    public static Object getCacheObj(String name){
 
    	Object ro =  getRedisTemplate().opsForValue().get(getRedisPreName()+name);
    	
    	return ro;
    }
    
    /**
     * 获取缓存对象
     * @param name
     * @param c
     * @param 秒
     * @return
     */
    public static <T> T getCacheObj(String name, Class<T> c){
 
    	Object ro =  getRedisTemplate().opsForValue().get(getRedisPreName()+name);
    	
    	if(ro != null){
    		return JsonUtil.readValue(ro.toString(), c);
    	}

    	return null;
    }
    

    /**
     * 保存缓存对象
     * @param name
     * @param c
     * @param 秒
     * @return
     */
    public static void setCacheObj(String name, Object o,long seconds){
    	 if( o == null) return;
    	 getRedisTemplate().opsForValue().set(getRedisPreName()+name, JsonUtil.writeValueAsString(o));
    	 getRedisTemplate().expire(getRedisPreName()+name, seconds,TimeUnit.SECONDS);
    }
    
    
    /**
     * 删除缓存对象
     * @param name
     * @param c
     * @return
     */
    public static void delCacheHashObj(String name ,String key){
    	 getRedisTemplate().opsForHash().delete(getRedisPreName()+name, key);
    }
    
    /**
     * 删除缓存根对象对象
     * @param name
     * @param c
     * @return
     */
    public static void delCache(String name ){
    	 getRedisTemplate().delete(getRedisPreName()+name);
    }
}
