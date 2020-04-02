package study.nosql.redis.jedis;

import redis.clients.jedis.Jedis;

public class JedisTest {

    public static void main(String[] args) {
        String key = "test";
        Jedis jedis = JedisUtils.getJedis();
        jedis.set(key, "Hello Work!");
        jedis.expire(key, 5);
        jedis.close();

    }
}
