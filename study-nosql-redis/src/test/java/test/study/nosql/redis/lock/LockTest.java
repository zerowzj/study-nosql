package test.study.nosql.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import study.nosql.redis.jedis.lock.JedisLock;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LockTest {

    public static void main(String[] args) {
        String lock = "666666";
        int clientNum = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(clientNum);
        final JedisLock jedisLock = new JedisLock();
        for (int i = 0; i < clientNum; i++) {
            executorService.execute(() -> {
                //通过Snowflake算法获取唯一的ID字符串
                String id = UUID.randomUUID().toString();
                try {
                    jedisLock.lock(lock, id);
                    try {
                        TimeUnit.SECONDS.sleep(20);
                    } catch (Exception ex) {

                    }
                } finally {
                    jedisLock.unlock(lock, id);
                }
            });
        }
    }
}
