package com.example.chatapplication.socket.module;


import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.chatapplication.common.Category;
import com.example.chatapplication.dto.response.LoginResponse;
import com.example.chatapplication.socket.datalistner.QRRawText;
import com.example.chatapplication.socket.datalistner.QrDataListener;
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

    }
    private ConnectListener onConnected(){
        return client -> {

            HandshakeData handshakeData = client.getHandshakeData();
            Long userId = Long.parseLong(handshakeData.getSingleUrlParam("userId"));

            log.info("Client[{}] - Connected to Chat module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
        };
    }
    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from Chat module.", client.getSessionId().toString());
        };
    }
}
