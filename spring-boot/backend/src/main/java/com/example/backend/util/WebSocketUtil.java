package com.example.backend.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.messaging.Message;

public class WebSocketUtil {

    private static final Map<String, Session> SESSION_USER_MAP = new ConcurrentHashMap<>();

    public static void addSession(Session session) {
        // 更新 SESSION_USER_MAP
        SESSION_USER_MAP.put(session.getId(), session);
    }

    public static void removeSession(Session session) {
        // 從 SESSION_USER_MAP 中移除
        SESSION_USER_MAP.remove(session.getId());

    }

    public static void broadcast(String message) {
        // 創建消息
        // String messageText = buildTextMessage(type, message);
        // 遍歷 SESSION_USER_MAP ，進行逐個發送
        for (String sessionId : SESSION_USER_MAP.keySet()) {
            sendTextMessage(SESSION_USER_MAP.get(sessionId), message);
        }
    }

    public static <T extends Message> void send(Session session, String type, T message) {
        // 創建消息
        String messageText = buildTextMessage(type, message);
        // 遍歷給單個 Session ，進行逐個發送
        sendTextMessage(session, messageText);
    }

    private static <T extends Message> String buildTextMessage(String type, T message) {
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", type);
        messageObject.put("body", message);
        return messageObject.toString();
    }

    private static void sendTextMessage(Session session, String messageText) {
        if (session == null) {
            return;
        }
        RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null) {
            return;
        }
        try {
            basic.sendText(messageText);
        } catch (IOException e) {
        }
    }

}