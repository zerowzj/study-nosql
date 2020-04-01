package study.nosql.redis.jedis;

import redis.clients.jedis.Jedis;

public class JedisTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("114.67.102.8", 7379);
        jedis.set("Jedis:key:123", "Hello Work!");
        System.out.println(jedis.get("Jedis:key:123JedisTest"));
        jedis.close();
    }
}
