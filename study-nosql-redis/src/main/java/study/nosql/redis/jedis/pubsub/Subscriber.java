package study.nosql.redis.jedis.pubsub;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

@Slf4j
public class Subscriber extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
        log.info("{}, {}", channel, message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
        log.info("fdasfasdf");
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        super.onPUnsubscribe(pattern, subscribedChannels);
        log.info("ffffffffffffff");
    }
}
