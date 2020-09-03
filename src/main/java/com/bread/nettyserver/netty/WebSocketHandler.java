package com.bread.nettyserver.netty;


import com.bread.nettyserver.config.NettyConfig;
import com.bread.nettyserver.utils.RabbitSendUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;


@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    RabbitSendUtil rabbitSendUtil;

    /**
     * 新客户端连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("WebSocketHandler handlerAdded node start {} user Size:{}",
                ctx.channel().id().asLongText(), NettyConfig.getChannelGroup().size());
        // 添加到channelGroup 通道组
        NettyConfig.getChannelGroup().add(ctx.channel());
    }

    /**
     * 读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        log.info("WebSocketHandler channelRead0 node ip:[{}] msg：{} ", clientIp, msg.text());
        /*
            将接收消息进行广播，各服务监听消息，判断是否需要处理应用
         */
        rabbitSendUtil.pendingSession(msg);
        /*
           接收客户端消息，进行具体业务逻辑处理
           ctx.channel().attr(key).setIfAbsent(value); // 将业务主键作为自定义属性加入到channel中，方便随时channel中获取对应业务信息
           ctx.channel().writeAndFlush("success");返回客户端数据
         */
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("WebSocketHandler handlerRemoved {}", ctx.channel().id().asLongText());
        // 删除通道
        NettyConfig.getChannelGroup().remove(ctx.channel());
        remove(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("WebSocketHandler exceptionCaught Exception：{}", cause.getMessage());
        // 删除通道
        NettyConfig.getChannelGroup().remove(ctx.channel());
        remove(ctx);
        ctx.close();
    }

    /**
     * 删除用户与channel的对应关系
     *
     * @param ctx
     */
    private void remove(ChannelHandlerContext ctx) {
        AttributeKey<String> key = AttributeKey.valueOf("key");
        String value = ctx.channel().attr(key).get();
        log.info("WebSocketHandler removeUserId start value:{}", value);
        if (!StringUtils.isEmpty(value)) {
            //删除用户连接
            NettyConfig.getUserChannelMap().remove(value);
            log.info("WebSocketHandler removeUserId end logout success value:{}",value);
            //发送广播消息,通知集群该客户端下线，防止一个客户端与多个节点进行连接
            rabbitSendUtil.pendingSession(value);
        }
    }
}
