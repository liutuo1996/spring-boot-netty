package com.bread.nettyserver.utils;

import com.alibaba.fastjson.JSONObject;
import com.bread.nettyserver.constant.RabbitConstant;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitSendUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 广播模式.
     *
     * @param data the data
     * @return the response entity
     */
    public void broadcastSend(String data) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstant.FANOUT_EXCHANGE, "", data, correlationData);
    }


    /**
     * 广播模式.
     *
     * @param appletPushMessage the appletPushMessage
     * @return the response entity
     */
    public void broadcastSend(Object appletPushMessage) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstant.FANOUT_EXCHANGE, "", JSONObject.toJSONString(appletPushMessage), correlationData);
    }


    public void send(Object appletPushMessage) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstant.TOPIC_EXCHANGE, RabbitConstant.ROUTING_KEY_MSG_CMD, JSONObject.toJSONString(appletPushMessage), correlationData);
    }


    /**
     * 广播模式.
     *
     * @param data
     * @return the response entity
     */
    public void pendingSession(String data) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstant.FANOUT_EXCHANGE_PENDING_SESSION, "", data, correlationData);
    }

    public void pendingSession(Object eventMessageRequest) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConstant.FANOUT_EXCHANGE_PENDING_SESSION, "", JSONObject.toJSONString(eventMessageRequest), correlationData);
    }
}
