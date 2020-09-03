package com.bread.nettyserver.netty.api.impl;


import com.bread.nettyserver.config.NettyConfig;
import com.bread.nettyserver.netty.api.PushCmd;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
public class PushCmdImpl implements PushCmd {

    @Override
    public void pushMsgToOne(String userId, String msg) {
        if (log.isInfoEnabled()) {
            log.info("PushCmdImpl pushMsgToOne userId:{},msg{}", userId, msg);
        }
        ConcurrentHashMap<String, Channel> userChannelMap = NettyConfig.getUserChannelMap();
        //判断是否本服务的连接
        Channel channel = userChannelMap.get(userId);
        if (channel != null) {
            if (log.isDebugEnabled()) {
                log.debug("PushCmdImpl pushMsgToOne userId:{},=========msg[{}]", userId, msg);
            }
            channel.writeAndFlush(new TextWebSocketFrame(msg));
        }
    }

    @Override
    public void pushMsgToAll(String msg) {
        NettyConfig.getChannelGroup().writeAndFlush(new TextWebSocketFrame(msg));
    }
}