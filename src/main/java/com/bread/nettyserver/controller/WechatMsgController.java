package com.bread.nettyserver.controller;

import com.bread.nettyserver.utils.RabbitSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: nettyserver
 * @description: 接收微信客服消息
 * @author: LiuTuo
 * @create: 2020-09-03 15:16
 **/
@Slf4j
@RestController
public class WechatMsgController {
    @Autowired
    private RabbitSendUtil rabbitSendUtil;

    /**
     * @param request
     * @param originalId 小程序原始ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/push/msg/{originalId}")
    public String pushMsg(HttpServletRequest request, @PathVariable("originalId") String originalId) {
        /*
            收到微信客服消息，广播通知各服务进行处理
         */
        rabbitSendUtil.pendingSession(request);
        return "success";
    }
}
