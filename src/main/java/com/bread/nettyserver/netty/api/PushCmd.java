package com.bread.nettyserver.netty.api;

public interface PushCmd {
    /**
     * 推送给指定用户
     *
     * @param userId
     * @param msg
     */
    void pushMsgToOne(String userId, String msg);

    /**
     * 推送给所有用户
     *
     * @param msg
     */
    void pushMsgToAll(String msg);
}
