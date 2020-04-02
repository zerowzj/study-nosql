package study.nosql.redis.jedis.id;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import study.nosql.redis.jedis.JedisUtils;

@Slf4j
public class IdUtils {

    public static long get() {
        return 1;
    }

    @Test
    public void test() {
        Jedis jedis = JedisUtils.getJedis();
        for (int i = 0; i < 100; i++) {
            log.info("dist_key= {}", jedis.incr("dist_key"));
        }
    }
}
