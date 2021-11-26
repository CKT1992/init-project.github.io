package com.example.backend.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;
import javax.websocket.RemoteEndpoint.Async;

public class WebSocketUtil {

    private static final Map<String, Session> SESSION_USER_MAP = new ConcurrentHashMap<>();

    public static void addSession(Session session) {
        // 更新 SESSION_USER_MAP
        SESSION_USER_MAP.put(session.getId(), session);
    }

    public static void removeSession(Session session) {
        // 從 SESSION_USER_MAP 中移除
        SESSION_USER_MAP.remove(session.getId(), session);

    }

    public static void broadcast(Session session, String message) throws IOException {
        if(session == null) {
            return;
        }
        Async async = session.getAsyncRemote();
        async.sendText(message);
    }
}