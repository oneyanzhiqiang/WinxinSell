package com.tycmb.cc.crs.service;

import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Created by 廖师兄
 * 2017-07-30 23:19
 */
@Component
@ServerEndpoint("/webSocket")
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    private  Log logger = LogFactory.getLog(WebSocket.class);
    
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        logger.info("【websocket消息】有新的连接, 总数:{}" + webSocketSet.size());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        logger.info("【websocket消息】连接断开, 总数:{}" + webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message) {
    	logger.info("【websocket消息】收到客户端发来的消息:{}" + message);
    }

    public void sendMessage(String message) {
        for (WebSocket webSocket: webSocketSet) {
        	logger.info("【websocket消息】广播消息, message={}" + message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
