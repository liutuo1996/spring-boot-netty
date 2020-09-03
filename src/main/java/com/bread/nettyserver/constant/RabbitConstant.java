package com.bread.nettyserver.constant;

public interface RabbitConstant {


    /**
     * 交换机 key
     */
    String FANOUT_EXCHANGE = "netty.fanout.exchange";


    String QUEUE_NAME_MSG = "netty.queue.name.msg";

    String TOPIC_EXCHANGE = "netty.topic.exchange.msg";

    String ROUTING_KEY_MSG_CMD = "netty.routingKey.msg.cmd";


    /**
     * 交换机 key （待接入会话更新）
     */
    String FANOUT_EXCHANGE_PENDING_SESSION = "netty.fanout.exchange.pending.session";


}
