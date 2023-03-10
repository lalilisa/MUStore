package com.example.chatapplication.common;

public enum Category {
    ;

    public enum SocketService{
        chat("chat");

        private final String name;
        SocketService(String name){
            this.name=name;
        }

    }
}
