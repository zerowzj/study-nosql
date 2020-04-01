package study.nosql.redis.jedis.pubsub;

import redis.clients.jedis.Jedis;

public class SubMain {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("114.67.102.8", 7379);
        jedis.subscribe(new Subscriber(), "TEST");
    }
}
