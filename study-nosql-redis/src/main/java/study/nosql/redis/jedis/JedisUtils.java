package study.nosql.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtils {

    private static String HOST = "114.67.102.8";

    private static int PORT = 7379;

    public static Jedis getJedis() {
        JedisPool pool = new JedisPool(HOST, PORT);
        return pool.getResource();
    }
}
