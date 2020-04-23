package com.atguigu.redistest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/* 1. JedisPoolUtil工具类，在其他主函数中用来创建pool;
*  2. pool: 用单例模式创建
*  3. 单例：经常用双端检索机制创建锁：保证单例一定就是单例
* */
public class JedisPoolUtil {

    private static volatile JedisPool jedisPool = null;  // 懒汉饿汉:就是这里直接new对象,或者静态单例创建instance方法中才创建

    private JedisPoolUtil() {}

    // 都是静态方法,因为外界没有办法通过创建这个类的实例调用方法,所以必须是静态方法
    public static JedisPool getJedisPoolInstance() {
        if (jedisPool == null) {
            synchronized (JedisPoolUtil.class) {
                if (jedisPool == null) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(100);
                    poolConfig.setMaxIdle(20);
                    poolConfig.setMaxWaitMillis(100*1000);
                    poolConfig.setTestOnBorrow(true); // 获得jedis实例时检查可用性(ping): 如果true,那么得到的jedis实例均可用

                    jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6381);
                }
            }
        }
        return jedisPool;
    }

    public static void release(JedisPool jedisPool, Jedis jedis) {
        if (jedis != null) {
            jedisPool.close();
        }
    }
}
