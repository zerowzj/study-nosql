package study.nosql.redis.jedis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.Jedis;

@Slf4j
public class DistKey {

    @Test
    public void test() {
        Jedis jedis = JedisUtils.getJedis();
        for (int i = 0; i < 100; i++) {
            log.info("dist_key= {}", jedis.incr("dist_key"));
        }
    }
}
