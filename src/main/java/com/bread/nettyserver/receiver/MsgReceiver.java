package com.bread.nettyserver.receiver;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MsgReceiver {

    @Value("${local.queueName}")
    String queueName;

    @Value("${local.pendingSession.queueName}")
    String pendingSessionQueueName;


    /**
     * FANOUT广播队列监听一.
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = {"${local.pendingSession.queueName}"})
    public void pendingSession(Message message, Channel channel) throws IOException {
        log.info("CsrMsgReceiver pendingSession pendingSessionQueueName:{} message body:{}", pendingSessionQueueName, new String(message.getBody()));
        try {
            /**
             * 针对消息做相关业务处理
             * 如推送消息给已连接客户端
             * pushCmd.pushMsgToOne(String.valueOf(eventMessageRequest.getUserId()), notificationMessage.toString());
             */


        } catch (Exception e) {
            log.error("MsgReceiver pendingSession Exception:", e);
        } finally {
            //消息确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
