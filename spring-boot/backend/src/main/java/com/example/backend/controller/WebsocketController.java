package com.example.backend.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.example.backend.exception.ValidateException;
import com.example.backend.util.WebSocketUtil;
import com.example.backend.viewModel.HeartbeatModel;
import com.example.backend.viewModel.TokenModel;
import com.google.gson.Gson;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import io.swagger.annotations.Api;

@Controller
@ServerEndpoint("/")
@Api(tags = "Websocket")
@CrossOrigin(origins = "http://localhost:9000")
public class WebsocketController {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException, InterruptedException {
        // 先登入
        URL login = new URL("http://www.eleplo.com:5580/eleplo/login");
        BufferedReader br = null;
        String token = null;

        BufferedReader hbbr = null;

        HttpURLConnection loginConn = (HttpURLConnection) login.openConnection();
        loginConn.setRequestMethod("POST");
        loginConn.setRequestProperty("Content-Type", "application/json; utf-8");

        String jsonInputString = "{\"username\":\"admin@wiadvance.com\",\"password\":\"12345678\"}";

        loginConn.setDoOutput(true);
        OutputStream os = loginConn.getOutputStream();
        os.write(jsonInputString.getBytes());
        os.flush();
        os.close();

        if (loginConn.getResponseCode() == 200) {
            br = new BufferedReader(new InputStreamReader(loginConn.getInputStream()));
            token = new Gson().fromJson(br.readLine(), TokenModel.class).getToken();
        } else {
            throw new ValidateException(HttpStatus.BAD_REQUEST, loginConn.getErrorStream().toString(), null);
        }
        while (token != null) {
            // 做事
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime());
            String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime() + 24 * 60 * 60 * 1000);
            URL heartbeat = new URL("http://www.eleplo.com:5580/eleplo/api/v2/heartRates/deviceId/175/startDate/"
                    + today + "/endDate/" + tomorrow);

            HttpURLConnection hbConn = (HttpURLConnection) heartbeat.openConnection();
            hbConn.setRequestProperty("Authorization", "Bearer " + token);
            hbConn.setRequestMethod("GET");

            if (hbConn.getResponseCode() == 200) {
                hbbr = new BufferedReader(new InputStreamReader(hbConn.getInputStream()));
                // 拆解資料
                HeartbeatModel[] list = new Gson().fromJson(hbbr.readLine(), HeartbeatModel[].class);
                List<HeartbeatModel> hrList = Arrays.asList(list);
                if (hrList.size() != 0) {
                    WebSocketUtil.broadcast(session, "{\"heartRate\":" + hrList.get(0).getHeartRate() + "}");
                    WebSocketUtil.removeSession(session);
                } else {
                    WebSocketUtil.broadcast(session, "{\"heartRate\":null}");
                    WebSocketUtil.removeSession(session);
                }
            }
            Thread.sleep(1000);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebSocketUtil.removeSession(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
        WebSocketUtil.removeSession(session);
    }
}