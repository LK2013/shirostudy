package com.imooc.session;

import com.imooc.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 重写sessionDao
 * @ClassName: RedsiSessionDao
 * @package session
 * @author: lk
 * @date: 2018/12/17 14:13
*/
public class RedsiSessionDao extends AbstractSessionDAO {
    @Autowired
    private JedisUtil jedisUtil;

    //sessionid前缀
    private final String shiro_session_prefix="imoo";

    private byte[] getKey(String key){
        return (shiro_session_prefix+key).getBytes();
    }

    private void saveSession(Session session){
        if(session!=null && session.getId()!=null) {
            byte[] key = getKey(session.getId().toString());

            byte[] value = SerializationUtils.serialize(session);

            jedisUtil.set(key, value);
            jedisUtil.expire(key, 600);
        }
    }
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId=generateSessionId(session);
        //捆绑sessionId和session
        assignSessionId(session,sessionId);
        saveSession(session);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId ==null){return  null;}
        byte[] key=getKey(sessionId.toString());
        byte[] value=jedisUtil.get(key);
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if(session !=null && session.getId()!=null){
            saveSession(session);
        }
    }

    @Override
    public void delete(Session session) {
        if(session !=null && session.getId()!=null){
            byte[] key=getKey(session.getId().toString());
            jedisUtil.del(key);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys=jedisUtil.keys(shiro_session_prefix);
        Set<Session> sessions=new HashSet<>();
        if(CollectionUtils.isEmpty(keys)){
            return null;
        }
        for(byte[] key:keys){
            Session session= (Session) SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }
        return sessions;

    }
}
