package study.nosql.redis.jedis;

import redis.clients.jedis.Jedis;

public class JedisTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("150.158.110.15", 6379);
        jedis.set("Jedis:key:123", "Hello Work!");
        System.out.println(jedis.get("Jedis"));
        jedis.close();
    }
}
