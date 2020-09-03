package com.bread.nettyserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@Slf4j
@SpringBootApplication
public class NettyserverApplication {

    /**
     * 容器启动，动态分配专属消费队列
     */
    static {
        String queueName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        log.info("local.queueName:{}", queueName);
        System.setProperty("local.queueName", "netty." + queueName);
        System.setProperty("local.pendingSession.queueName", "netty.ps" + queueName);
    }

    public static void main(String[] args) {
        SpringApplication.run(NettyserverApplication.class, args);
    }

}
