package study.nosql.redis.jedis.event;

import redis.clients.jedis.Jedis;
import study.nosql.redis.jedis.JedisUtils;

public class EventMain {

    public static void main(String[] args) {
        Jedis jedis = JedisUtils.getJedis();
//        jedis.subscribe(new KeySpaceListener(), "__keyspace@0__:mykey");
//        jedis.subscribe(new KeyEventListener(), "__keyevent@0__:expired");
        jedis.psubscribe(new KeySpaceListener(), "__keyspace@0__:mykey");
    }
}
