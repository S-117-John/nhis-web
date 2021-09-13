package com.zebone.platform.framework.security.redis;

import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.utils.RedisUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @author lingjun
 * @project video
 * @create_date 2014-5-6 下午5:35:07
 */
public class RedisSessionDAO extends AbstractSessionDAO {


	private long timeout = 1800000L;


	 static Logger logger = LogManager.getLogger(RedisSessionDAO.class.getName());
    /**
     * The Redis key prefix for the sessions 
     */
    private String keyPrefix = "shiro_redis_session:";
    
    private String keyUserPrefix = "redis_user:";
     
    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }
     
    /**
     * save session
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }
        
       // byte[] key = getByteKey(session.getId());
        String key = getStrKey(session.getId());
        byte[] value = SerializeUtils.serialize(session);
        
        
        RedisUtils.getDefaultRedisTemplate().opsForValue().set(key, value, timeout/1000, TimeUnit.SECONDS);
        
        session.setTimeout(timeout);   
        
        Object user = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if(user != null && user instanceof SimplePrincipalCollection){
        	User u = (User)((SimplePrincipalCollection)user).getPrimaryPrincipal();
        	RedisUtils.getRedisTemplate().opsForValue().set(RedisUtils.getRedisPreName() + keyUserPrefix+ u.getLoginName(), session.getId().toString(), timeout/1000, TimeUnit.SECONDS);
        }
}
 
    @Override
    public void delete(Session session) {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }
        RedisUtils.getDefaultRedisTemplate().delete(this.getStrKey(session.getId()));
        String username = (String)session.getAttribute("SHIRO_USERNAME");
        if(username != null && !"".equals(username)){
            RedisUtils.getRedisTemplate().delete(RedisUtils.getRedisPreName() + keyUserPrefix+ username);
        }
    }
 
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();
        Set<?> keys = RedisUtils.getDefaultRedisTemplate().keys( RedisUtils.getRedisPreName() + this.keyPrefix + "*");
        if(keys != null && keys.size()>0){
            keys.forEach(k-> sessions.add((Session) SerializeUtils.deSerialize((byte[]) RedisUtils.getDefaultRedisTemplate().opsForValue().get(k))));
        }
        return sessions;
    }
 
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);  
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }
 
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId == null){
            logger.error("session id is null");
            return null;
        }
        Object obj = RedisUtils.getDefaultRedisTemplate().opsForValue().get(this.getStrKey(sessionId));
         if(obj == null){
        	 return null;
         }
        Session s = (Session) SerializeUtils.deSerialize((byte[])obj);
        return s;
    }
     
    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private byte[] getByteKey(Serializable sessionId){
        String preKey = RedisUtils.getRedisPreName() +  this.keyPrefix + sessionId;
        return preKey.getBytes();
    }
 
    
    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private String getStrKey(Serializable sessionId){
        String preKey = RedisUtils.getRedisPreName() +  this.keyPrefix + sessionId;
        return preKey;
    }
 
    
    
 
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

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
     
     
}