package study.nosql.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtils {

    private static String host = "114.67.102.8";

    private static int port = 7379;

    public static Jedis getJedis() {
        JedisPool pool = new JedisPool(host, port);
        return pool.getResource();
    }
}
