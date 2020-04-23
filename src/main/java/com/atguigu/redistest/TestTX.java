package com.atguigu.redistest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestTX {
    public boolean transMethod() throws InterruptedException {
        Jedis jedis = new Jedis("127.0.0.1", 6381); // 主从，那么这里只能往master中写
        int balance;
        int debt;
        int amountToSubstract = 10;

        jedis.watch("balance");
        // jedis.set("balance", String.valueOf(5)); // 模拟破坏事务完整
        Thread.sleep(7000);
        balance = Integer.parseInt(jedis.get("balance"));
        if (balance < amountToSubstract) {
            jedis.unwatch();
            System.out.printf("value modified");
            return false;
        } else {
            System.out.printf("*********transaction**********");
            Transaction transaction = jedis.multi();
            transaction.incrBy("debt", amountToSubstract);
            transaction.decrBy("balance", amountToSubstract);
            transaction.exec();
            balance = Integer.parseInt(jedis.get("balance"));
            debt = Integer.parseInt(jedis.get("debt"));
            System.out.println(String.valueOf(balance));
            System.out.println(String.valueOf(debt));
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestTX testTX = new TestTX();
        boolean b =  testTX.transMethod();
        System.out.printf(String.valueOf(b));
    }
}
