package com.imooc.cache;

import com.imooc.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.Set;
@Component
public class RedisCache implements Cache {

    @Autowired
    private JedisUtil jedisUtil;

    private final String cache_prefix="imooc-cache:";

    private byte[] getkey(Object k){
        if(k instanceof String){
            return (cache_prefix+k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    @Override
    public Object get(Object o) throws CacheException {
        System.out.println("从redis中获取权限"+o);
        byte[] value=jedisUtil.get(getkey(o));
        if(value!=null){
            return SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public Object put(Object o, Object o2) throws CacheException {
        byte[] key=getkey(o);
        byte[] value=SerializationUtils.serialize(o2);
        System.out.println(key);
        jedisUtil.set(key,value);
        jedisUtil.expire(key,600);
        return value;
    }

    @Override
    public Object remove(Object o) throws CacheException {
        byte[] key=getkey(o);
        byte[] value=jedisUtil.get(key);
        jedisUtil.del(key);
        if(value!=null){
            return SerializationUtils.deserialize(value);
        }
        return value;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set keys() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }
}
