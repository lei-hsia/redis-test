package com.atguigu.redistest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestPool {
    public static void main(String[] args) {

        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        /*JedisPool jedisPool1= JedisPoolUtil.getJedisPoolInstance();
        System.out.printf(String.valueOf(jedisPool == jedisPool1));*/  // 测试单例模式生效

        Jedis  jedis = null;

        try{
            jedis = jedisPool.getResource();
            jedis.set("aa", "bb");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisPoolUtil.release(jedisPool, jedis);
        }
    }
}
