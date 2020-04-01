package study.nosql.redis.jedis.pubsub;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

@Slf4j
public class Subscriber extends JedisPubSub {

    /**
     *
     */
    @Override
    public void onMessage(String channel, String message) {
        log.info("channel={}, message={}", channel, message);
    }

    /**
     *
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info("fdasfasdf");
    }

    /**
     *
     */
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        log.info("ffffffffffffff");
    }
}
