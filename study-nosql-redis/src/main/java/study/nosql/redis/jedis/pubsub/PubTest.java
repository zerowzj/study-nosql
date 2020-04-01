package study.nosql.redis.jedis.pubsub;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import study.nosql.redis.jedis.JedisUtils;

public class PubTest {

    @Test
    public void test() {
        Jedis jedis = JedisUtils.getJedis();
        jedis.publish("TEST", "111111111111111");
        jedis.close();
    }
}
