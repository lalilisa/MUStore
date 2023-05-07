package com.example.chatapplication.socket.chat;


import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.chatapplication.common.Category;
import com.example.chatapplication.dto.response.LoginResponse;
import com.example.chatapplication.socket.datalistner.QRRawText;
import com.example.chatapplication.socket.datalistner.QrDataListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component

public class ChatModule {

    private final SocketIONamespace  namespace;

    private static final Logger log = LoggerFactory.getLogger(ChatModule.class);
    @Autowired
    public ChatModule(SocketIOServer socketIOServer){
        this.namespace=socketIOServer.addNamespace(Category.SocketService.chat.name);
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        onListeningVerifiQr();
    }
    private ConnectListener onConnected(){
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("Client[{}] - Connected to Chat module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
        };
    }
    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from Chat module.", client.getSessionId().toString());
        };
    }

    public void onListeningVerifiQr(){
        String token = null;
        log.info("Verifi Qr");
        this.namespace.addEventListener("verifi-qr", QRRawText.class, (client1, data, ackSender) -> {
            client1.sendEvent("authen", "sss");
            namespace.addEventListener("confirm", QrDataListener.class, (client, data1, ackSender1) -> {
                client.sendEvent("login-response",
                        LoginResponse.builder()
                                .accessToken("ss")
                                .userId(10L)
                                .role(Category.Role.ADMIN)
                                .refreshToken("ss")
                                .expireAccressToken(new Date())
                                .build());
            });
        });
    }
    public void send(){
        this.namespace.getBroadcastOperations().sendEvent("all","trimai");
    }
}
