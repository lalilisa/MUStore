package com.example.chatapplication.socket.chat;


import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.chatapplication.common.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatModule {

    private final SocketIONamespace  namespace;

    @Autowired
    public ChatModule(SocketIOServer socketIOServer){
        this.namespace=socketIOServer.addNamespace(Category.SocketService.chat.name());
        this.namespace.addListeners(onConnected());
        this.namespace.addListeners(onDisconnected());

    }
    private ConnectListener onConnected(){
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.debug("Client[{}] - Connected to Chat module through '{}'", client.getSessionId().toString(),
                    handshakeData.getUrl());
        };
    }
    private DisconnectListener onDisconnected() {
        return client -> {
            log.debug("Client[{}] - Disconnected from Chat module.", client.getSessionId().toString());
        };
    }
}
