package study.nosql.redis.jedis.pubsub;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class PubTest {

    @Test
    public void test() {
        Jedis jedis = new Jedis("114.67.102.8", 7379);
        jedis.publish("TEST", "FDSAFADSFA");
        jedis.close();
    }
}
