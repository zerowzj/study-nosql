package study.nosql.redis.jedis.pubsub;

import redis.clients.jedis.Jedis;
import study.nosql.redis.jedis.JedisUtils;

public class SubMain {

    public static void main(String[] args) {
        Jedis jedis = JedisUtils.getJedis();
        jedis.subscribe(new Subscriber(), "TEST");
    }
}
