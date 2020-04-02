package study.nosql.redis.jedis.event;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import study.nosql.redis.jedis.JedisUtils;

public class EventTest {

    @Test
    public void test() {
        String key = "lesson:123";
        Jedis jedis = JedisUtils.getJedis();
        jedis.set(key, "Hello Work!");
        jedis.expire(key, 5);
        jedis.close();
    }
}
