package com.bread.nettyserver.config;

import com.bread.nettyserver.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Configuration
public class RabbitConfig {

    @Value("${local.queueName}")
    String quenUuidName;

    @Value("${local.pendingSession.queueName}")
    String pendingSessionQueueName;

    /**
     * A. 声明 fanout 交换机.
     *
     * @return the exchange
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return (FanoutExchange) ExchangeBuilder.fanoutExchange(RabbitConstant.FANOUT_EXCHANGE).durable(true).build();
    }


    /**
     * A. Fanout queue
     * .autoDelete() 无消费者时自动删除队列，即重启服务后队列消失
     * @return the queue
     */
    @Bean
    public Queue fanoutQueueA() {
        return QueueBuilder.durable(quenUuidName).autoDelete().build();
    }

    /**
     * A.绑定队列A 到Fanout 交换机.
     *
     * @return the binding
     */
    @Bean
    public Binding bindingA() {
        return BindingBuilder.bind(fanoutQueueA()).to(fanoutExchange());
    }


    /**
     * B. 声明 fanout 交换机.
     *
     * @return the exchange
     */
    @Bean
    public FanoutExchange fanoutExchangeB() {
        return (FanoutExchange) ExchangeBuilder.fanoutExchange(RabbitConstant.FANOUT_EXCHANGE_PENDING_SESSION).durable(true).build();
    }


    /**
     * B. Fanout queue
     *
     * @return the queue
     */
    @Bean
    public Queue fanoutQueueB() {
        return QueueBuilder.durable(pendingSessionQueueName).autoDelete().build();
    }

    /**
     * B.绑定队列A 到Fanout 交换机.
     *
     * @return the binding
     */
    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(fanoutQueueB()).to(fanoutExchangeB());
    }


    @Bean
    public Queue queueOrderStatus() {
        return new Queue(RabbitConstant.QUEUE_NAME_MSG);
    }

    @Bean
    public TopicExchange topicExchangeOrderStatus() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE);
    }

    @Bean
    public Binding bindingOrderStatus() {
        return BindingBuilder.bind(queueOrderStatus()).to(topicExchangeOrderStatus()).with(RabbitConstant.ROUTING_KEY_MSG_CMD);
    }


}
