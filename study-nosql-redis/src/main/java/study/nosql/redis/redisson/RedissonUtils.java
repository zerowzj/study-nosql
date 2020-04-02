package study.nosql.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonUtils {

    private static String HOST = "114.67.102.8";
    private static int PORT = 7379;

    public static RedissonClient getRedisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(HOST + ":" + PORT);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
