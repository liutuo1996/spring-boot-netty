package com.bread.nettyserver.config;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

public class NettyConfig {
    /**
     * 定义一个channel组，管理所有的channel
     * GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
     */
    private final static ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存放用户与Chanel的对应信息，用于给指定用户发送消息
     */
    private final static ConcurrentHashMap<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<>(100);

    private NettyConfig() {
    }

    /**
     * 获取channel组
     *
     * @return
     */
    public static ChannelGroup getChannelGroup() {
        return CHANNEL_GROUP;
    }

    /**
     * 获取用户channel map
     *
     * @return
     */
    public static ConcurrentHashMap<String, Channel> getUserChannelMap() {
        return USER_CHANNEL_MAP;
    }

    /**
     * 判断当前userId存不存在当前 USER_CHANNEL_MAP
     *
     * @param userId
     * @return
     */
    public static boolean isUserContains(String userId) {
        ConcurrentHashMap<String, Channel> userChannelMap = NettyConfig.getUserChannelMap();
        return userChannelMap.containsKey(userId);
    }
}