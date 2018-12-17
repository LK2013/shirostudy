package com.imooc.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * redis工具类
 * @ClassName: JedisUtil
 * @package com.imooc.util
 * @author: lk
 * @date: 2018/12/17 14:20
*/
@Component
public class JedisUtil {
    @Autowired
    private JedisPool jedisPool;

    private Jedis getResource(){
        return jedisPool.getResource();
    }

    public byte[] set(byte[] key, byte[] value) {

        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return value;
    }
    //设置key的指定超时时间
    public byte[] expire(byte[] key, int seconds) {

        Jedis jedis = getResource();
        try {
            jedis.expire(key,seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return key;
    }

    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try {
            byte[] bytes = jedis.get(key);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return null;
    }

    public byte[] del(byte[] key) {
        Jedis jedis = getResource();
        try {
            Long bytes = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return null;
    }

    public Set<byte[]> keys(String prefix) {
        Jedis jedis = getResource();
        try {
            return jedis.keys((prefix+"*").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return null;
    }
}
