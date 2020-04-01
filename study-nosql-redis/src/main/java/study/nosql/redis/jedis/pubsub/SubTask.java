package study.nosql.redis.jedis.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SubTask implements Runnable {

    private JedisPool jedisPool;

    private String channel;

    private Subscriber subscriber;

    public SubTask(JedisPool jedisPool, String channel, Subscriber subscriber) {
        this.jedisPool = jedisPool;
        this.channel = channel;
        this.subscriber = subscriber;
    }

    public void run() {
        Jedis jedis;
        try {
            jedis = jedisPool.getResource();
            jedis.subscribe(subscriber, channel);
        } catch (Exception ex) {

        } finally {

        }
    }
}
