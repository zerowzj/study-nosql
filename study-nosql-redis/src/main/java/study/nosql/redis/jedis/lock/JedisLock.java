package study.nosql.redis.jedis.lock;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import study.nosql.redis.jedis.JedisUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JedisLock {

    /* 锁过期时间 */
    private static final long internalLockLeaseTime = 30000;

    /* 获取锁的超时时间 */
    private static long TIME_OUT = 999999;

    /* SET命令的参数 */
    private static SetParams PARAMS = SetParams.setParams()
            .nx()
            .px(internalLockLeaseTime);

    /**
     * 加锁
     *
     * @param lockKey  锁
     * @param identity 身份标识
     */
    public boolean lock(String lockKey, String identity) {
        Jedis jedis = JedisUtils.getJedis();
        Long start = System.currentTimeMillis();
        try {
            for (; ; ) {
                //SET命令返回OK，则证明获取锁成功
                String reply = jedis.set(lockKey, identity, PARAMS);
                if ("OK".equalsIgnoreCase(reply)) {
                    log.info("id={} 获取到了锁 lock={}", identity, lockKey);
                    return true;
                }

                //否则循环等待，在timeout时间内仍未获取到锁，则获取失败
                long time = System.currentTimeMillis() - start;
                if (time >= TIME_OUT) {
                    log.info("id={} 超时退出对锁竞争 lock={}", identity, lockKey);
                    return false;
                }
                log.info("id={} 自旋后继续锁竞争 lock={}", identity, lockKey);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            jedis.close();
        }
    }

    /**
     * 解锁
     *
     * @param lockKey
     * @param identity
     */
    public boolean unlock(String lockKey, String identity) {
        Jedis jedis = JedisUtils.getJedis();
        StringBuffer script = new StringBuffer();

        script.append("if redis.call('get',KEYS[1]) == ARGV[1] then")
                .append("   return redis.call('del',KEYS[1]) ")
                .append("else")
                .append("   return 0 ")
                .append("end");
        try {
            List<String> keys = Collections.singletonList(lockKey);
            List<String> args = Collections.singletonList(identity);
            Object result = jedis.eval(script.toString(), keys, args);
            if ("1".equals(result.toString())) {
                log.info("id={} 释放了锁 lock={}", identity, lockKey);
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }
    }
}
